package com.avinash.danumalk.user;

import jakarta.mail.MessagingException;

import java.security.Principal;

public interface UserServiceInterface {
    void changePassword(ChangePasswordRequest request, Principal connectedUser);
    void disableUser(UserStatusChangeRequest disableUserRequest);
    void enableUser(UserStatusChangeRequest enableUserRequest) throws MessagingException;
    UserResponse updateUserProfile(Integer id, UserRequest editUserRequest) ;


}
