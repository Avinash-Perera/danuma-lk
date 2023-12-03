package com.avinash.danumalk.auth;

import com.avinash.danumalk.config.JwtService;
import com.avinash.danumalk.exceptions.DuplicateEmailException;
import com.avinash.danumalk.exceptions.IncorrectCredentialsException;
import com.avinash.danumalk.exceptions.OnlyOneAdminAllowedException;
import com.avinash.danumalk.token.Token;
import com.avinash.danumalk.token.TokenRepository;
import com.avinash.danumalk.token.TokenType;
import com.avinash.danumalk.user.Role;
import com.avinash.danumalk.user.User;
import com.avinash.danumalk.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    /**
     * Registers a new user based on the provided registration request.
     *
     * @param  request  the registration request containing the user details
     * @return          the authentication response containing the access token and refresh token
     */
    public AuthenticationResponse register(RegisterRequest request) {
        // Check if there is already an admin user
        if (request.getRole() == Role.ADMIN && repository.findByRole(Role.ADMIN).isPresent()) {
            throw new OnlyOneAdminAllowedException("Only one admin user is allowed.");
        }
        try {
            var user = User.builder()
                    .usersName(request.getUsersName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(request.getRole())
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

    /**
     * Authenticates the user based on the provided authentication request.
     *
     * @param  request  The authentication request containing the user's email and password.
     * @return          The authentication response containing the access token and refresh token.
     */
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
            throw new IncorrectCredentialsException("Incorrect username or password");
        }
    }

    /**
     * Saves the user token into the token repository.
     *
     * @param  user      the user object
     * @param  jwtToken  the JWT token string
     */
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

    /**
     * Revokes all tokens for a given user.
     *
     * @param  user  the user for whom to revoke tokens
     */
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

    /**
     * Refreshes the authentication token.
     *
     * @param  request   the HttpServletRequest object representing the incoming request
     * @param  response  the HttpServletResponse object representing the outgoing response
     * @throws IOException  if an input or output exception occurs
     */
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
}

