package com.avinash.danumalk.user;

import com.avinash.danumalk.common.PageResponse;
import jakarta.mail.MessagingException;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface UserServiceInterface {
    void changePassword(ChangePasswordRequest request, Principal connectedUser);
    void disableUser(UserStatusChangeRequest disableUserRequest);
    void enableUser(UserStatusChangeRequest enableUserRequest) throws MessagingException;
    UserResponse updateUserProfile(UUID id, UserRequest editUserRequest) ;
    UserResponse getCurrentUser(UserDetails userDetails);
    PageResponse<UserResponse> getAllUsers(int page, int size);
    PageResponse<UserResponse> getAllUsersForAdmin(int page, int size);
    void deleteUser(UUID id, UserDetails userDetails);
}
