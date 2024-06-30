package com.avinash.danumalk.auth;

import com.avinash.danumalk.config.JwtService;
import com.avinash.danumalk.email.EmailService;
import com.avinash.danumalk.email.EmailTemplateName;
import com.avinash.danumalk.exceptions.DuplicateAdminException;
import com.avinash.danumalk.exceptions.DuplicateEmailException;
import com.avinash.danumalk.exceptions.IncorrectCredentialsException;
import com.avinash.danumalk.exceptions.UserNotFoundException;
import com.avinash.danumalk.role.RoleRepository;
import com.avinash.danumalk.token.Token;
import com.avinash.danumalk.token.TokenRepository;
import com.avinash.danumalk.token.TokenType;
import com.avinash.danumalk.user.User;
import com.avinash.danumalk.user.UserRepository;
import com.avinash.danumalk.util.UserUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements AuthenticationServiceInterface {
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final RoleRepository roleRepository;
    private final UserUtils userUtils;

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl ;

    @Value("${application.mailing.frontend.forgot-password-url}")
    private  String forgot_password_url;

    @Override
    public AuthenticationResponse registerAdmin(RegisterRequest request) {

        var userRole = roleRepository.findByName("ADMIN")
                // todo - better exception handling
                .orElseThrow(() -> new IllegalStateException("ROLE USER was not initiated"));

        // Check if an admin user already exists
        var existingAdmin = repository.findByRoles_Name("ADMIN");
        if (existingAdmin.isPresent()) {
            throw new DuplicateAdminException("An admin user already exists.");
        }
        try {
            var user = User.builder()
                    .usersName(request.getUsersName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .enabled(true)
                    .accountLocked(false)
                    .roles(List.of(userRole))
                    .build();
            var savedUser = repository.save(user);
            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
            saveUserToken(savedUser, jwtToken);
            return AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch ( DataIntegrityViolationException e) {
            // Handle the case where the email is already taken
            throw new DuplicateEmailException("Email is already taken");
        }
    }

    @Override
    public boolean register(RegisterRequest request) throws MessagingException {
        var userRole = roleRepository.findByName("USER")
                // todo - better exception handling
                .orElseThrow(() -> new IllegalStateException("ROLE USER was not initiated"));
        try {
            var user = User.builder()
                    .usersName(request.getUsersName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .accountLocked(false)
                    .enabled(false)
                    .roles(List.of(userRole))
                    .build();
            var savedUser = repository.save(user);
            String newToken = userUtils.generateAndSaveActivationToken(savedUser);
            userUtils.sendValidationEmail(savedUser, newToken);
            return true;
        } catch (DataIntegrityViolationException e) {
            // Handle the case where the email is already taken
            throw new DuplicateEmailException("Email is already taken");
        } catch (MessagingException e) {
            // Re-throw MessagingException to be handled by the caller
            throw e;
        } catch (Exception e) {
            // Handle any other exceptions
            throw new RuntimeException("An unexpected error occurred during registration", e);
        }
    }

    @Override
    @Transactional
    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token)
                // todo exception has to be defined
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            userUtils.sendValidationEmail(savedToken.getUser(), userUtils.generateAndSaveActivationToken(savedToken.getUser()));
            throw new RuntimeException("Activation token has expired. A new token has been send to the same email address");
        }

        var user = repository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEnabled(true);
        repository.save(user);

        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }


    @Override
    public void resendActivationCode(String email) throws MessagingException {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        if (user.isEnabled()) {
            throw new IllegalStateException("User is already enabled");
        }

        String newToken = userUtils.generateAndSaveActivationToken(user);
        userUtils.sendValidationEmail(user, newToken);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            var user = repository.findByEmail(request.getEmail())
                    .orElseThrow();
            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
            revokeAllUserTokens(user);
            saveUserToken(user, jwtToken);
            return AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (AuthenticationException e) {
            // Handle incorrect username or password
            throw new IncorrectCredentialsException("Incorrect username or password or not activated");
        }
    }


    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }



    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }


    @Override
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.repository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    //Reset password
    public void sendPasswordResetEmail(String email) throws MessagingException {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String resetToken = userUtils.generateAndSaveActivationToken(user);

        emailService.sendPasswordResetEmail(
                user.getEmail(),
                user.getUsersName(),
                EmailTemplateName.FORGOT_PASSWORD,
                forgot_password_url,
                resetToken,
                "Password Reset Request"
        );
    }

    public void resetPassword(String token, String newPassword) {
        Token resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (LocalDateTime.now().isAfter(resetToken.getExpiresAt())) {
            throw new RuntimeException("Reset token has expired");
        }

        User user = repository.findById(resetToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        repository.save(user);

        resetToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(resetToken);
    }





}

