package com.avinash.danumalk.user;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PatchMapping
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    ) {
        service.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/disable_user")
    public ResponseEntity<String> disableUser(@RequestBody UserStatusChangeRequest disableUserRequest) {
        service.disableUser(disableUserRequest);
        return ResponseEntity.ok("User disabled successfully");
    }

    @PatchMapping("/enable_user")
    public ResponseEntity<String> enableUser(@RequestBody UserStatusChangeRequest enableUserRequest) throws MessagingException {
        service.enableUser(enableUserRequest);
        return ResponseEntity.ok("User Activation Email sent");
    }


}
