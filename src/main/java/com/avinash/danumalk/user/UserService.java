package com.avinash.danumalk.user;

import com.avinash.danumalk.common.PageResponse;
import com.avinash.danumalk.email.EmailService;
import com.avinash.danumalk.exceptions.CannotChangeStatusException;
import com.avinash.danumalk.exceptions.UserNotFoundException;
import com.avinash.danumalk.role.RoleName;
import com.avinash.danumalk.token.TokenRepository;
import com.avinash.danumalk.util.UserUtils;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final UserMapper userMapper;
    private final ImageHelper imageHelper;
    private final UserUtils userUtils;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;





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

        // Ensure that the current user is disabling their own account
        if (!disableUserRequest.email().equals(user.getEmail())) {
            throw new CannotChangeStatusException("Email is not associated with your account");
        }
        user.setEnabled(false);
        repository.save(user);





    }

    public void enableUser(UserStatusChangeRequest enableUserRequest) throws MessagingException {
        User user = repository.findByEmail(enableUserRequest.email())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + enableUserRequest.email()));
        // Ensure that the current user is enabling their own account
        if (!enableUserRequest.email().equals(user.getEmail())) {
            throw new CannotChangeStatusException("Email is not associated with your account");
        }

        String newToken = userUtils.generateAndSaveActivationToken(user);
        userUtils.sendValidationEmail(user, newToken);

    }


    @Override
    public UserResponse updateUserProfile(UUID id, UserRequest updateRequest) {
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

    @Override
    public UserResponse getCurrentUser(UserDetails userDetails){
        String email = userDetails.getUsername();
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        return userMapper.toUserResponse(user);
    }

    @Override
    public PageResponse<UserResponse> getAllUsers(int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<User> users = repository.findAllByEnabled(true, pageable);
        List<UserResponse> usersAsResponse = users.stream()
                .map(userMapper::toUserResponse)
                .toList();
        return new PageResponse<>(
                usersAsResponse,
                users.getNumber(),
                users.getSize(),
                users.getTotalElements(),
                users.getTotalPages(),
                users.isFirst(),
                users.isLast()
        );
    }


    @Override
    public PageResponse<UserResponse> getAllUsersForAdmin(int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<User> users = repository.findAll(pageable);
        List<UserResponse> usersAsResponse = users.stream()
                .map(userMapper::toUserResponse)
                .toList();
        return new PageResponse<>(
                usersAsResponse,
                users.getNumber(),
                users.getSize(),
                users.getTotalElements(),
                users.getTotalPages(),
                users.isFirst(),
                users.isLast()
        );
    }

    @Override
    @Transactional
    public void deleteUser(UUID id, UserDetails userDetails) {
        var userToDelete = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        var adminUser = repository.findByRoles_Name(String.valueOf(RoleName.ADMIN))
                .orElseThrow(() -> new UserNotFoundException("Admin user not found with email: " + userDetails.getUsername()));

        if (userToDelete.getId().equals(adminUser.getId())) {
            throw new CannotChangeStatusException("Admin cannot delete themselves");
        }

        // Delete related tokens
        tokenRepository.deleteByUserId(id);

        repository.delete(userToDelete);

        // Send account deletion email
        try {
            emailService.sendAccountDeletionEmail(userToDelete.getEmail(), userToDelete.getUsersName(), "Your Account Has Been Deleted");
        } catch (MessagingException e) {
            log.error("Failed to send account deletion email to {}: {}", userToDelete.getEmail(), e.getMessage());
            // Optionally, rethrow or handle the exception based on your needs
        }
    }


}
