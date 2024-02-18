package com.avinash.danumalk.user;

import java.security.Principal;

public interface UserServiceInterface {
    void changePassword(ChangePasswordRequest request, Principal connectedUser);
}
