package com.avinash.danumalk.user;

import com.avinash.danumalk.auth.AuthenticationService;
import com.avinash.danumalk.email.EmailService;
import com.avinash.danumalk.email.EmailTemplateName;
import com.avinash.danumalk.exceptions.UserNotFoundException;
import com.avinash.danumalk.token.Token;
import com.avinash.danumalk.token.TokenRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final AuthenticationService authenticationService;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final UserMapper userMapper;
    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl ;
    private final ImageHelper imageHelper;


    @Override
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        repository.save(user);
    }

    public void disableUser(UserStatusChangeRequest disableUserRequest) {
        User user = repository.findByEmail(disableUserRequest.email())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + disableUserRequest.email()));
        user.setEnabled(false);
        repository.save(user);
    }

    public void enableUser(UserStatusChangeRequest enableUserRequest) throws MessagingException {
        User user = repository.findByEmail(enableUserRequest.email())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + enableUserRequest.email()));
        sendValidationEmail(user);

    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);
        System.out.println(Optional.of(newToken));

        emailService.sendEmail(
                user.getEmail(),
                user.getUsersName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation"
        );
    }

    private String generateAndSaveActivationToken(User user) {
        // Generate a token
        String generatedToken = generateActivationCode(6);
        System.out.println(generatedToken);
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);
        System.out.println(token);


        return generatedToken;
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();

        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }

    @Override
    public UserResponse updateUserProfile(Integer id, UserRequest updateRequest) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        String currentProfileImageUrl = user.getProfile_image_url();
        String requestProfileImageUrl = updateRequest.profile_image_url();

        log.info("Updating profile for user with id: {}", id);

        // Handle the case where the profile image URL has been updated
        if (requestProfileImageUrl != null && !requestProfileImageUrl.isEmpty()) {
            if (currentProfileImageUrl != null && !currentProfileImageUrl.isEmpty() && !currentProfileImageUrl.equals(requestProfileImageUrl)) {
                log.info("Deleting old profile image: {}", currentProfileImageUrl);
                imageHelper.deleteImageFromUserDirectory(currentProfileImageUrl, user.getId());
            }

            if (imageHelper.isImageInTempDirectory(requestProfileImageUrl)) {
                log.info("Moving new profile image from temp directory: {}", requestProfileImageUrl);
                imageHelper.moveProfileImageToUserFolder(requestProfileImageUrl, user.getId());
            } else if (imageHelper.isImageInUserDirectory(requestProfileImageUrl, user.getId())) {
                log.info("Profile image already in user's directory: {}", requestProfileImageUrl);
            } else {
                log.error("Profile image not found in temp or user directory: {}", requestProfileImageUrl);
                throw new IllegalStateException("Profile image not found in temp or user directory");
            }
        }

        user = userMapper.updateUserFromRequest(updateRequest, user);
        repository.save(user);
        log.info("Profile updated successfully for user with id: {}", id);
        return userMapper.toUserResponse(user);
    }


}
