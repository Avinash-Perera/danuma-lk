package com.avinash.danumalk.user;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.avinash.danumalk.common.PageResponse;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
public class UserController {

    private final UserService service;

    @PatchMapping
    @PreAuthorize("hasAuthority('admin:update') or hasAuthority('user:update')")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    ) {
        service.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/disable_user")
    @PreAuthorize("hasAuthority('admin:update') or hasAuthority('user:update')")
    public ResponseEntity<String> disableUser(@RequestBody UserStatusChangeRequest disableUserRequest) throws Exception {
        service.disableUser(disableUserRequest);
        return ResponseEntity.ok("User disabled successfully");
    }

    @PatchMapping("/enable_user")
    @PreAuthorize("hasAuthority('admin:update') or hasAuthority('user:update')")
    public ResponseEntity<String> enableUser(@RequestBody UserStatusChangeRequest enableUserRequest) throws MessagingException {
        service.enableUser(enableUserRequest);
        return ResponseEntity.ok("User Activation Email sent");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:update') or hasAuthority('user:update')")
    public ResponseEntity<UserResponse> updateUserProfile(@PathVariable UUID id, @RequestBody UserRequest updateRequest) {
        UserResponse response = service.updateUserProfile(id, updateRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('admin:read') or hasAuthority('user:read')")
    public UserResponse getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return service.getCurrentUser(userDetails);
    }


    @GetMapping
    @PreAuthorize("hasAuthority('user:read') and !hasRole('ADMIN')")
    public ResponseEntity<PageResponse<UserResponse>> getAllUsers(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok(service.getAllUsers(page,size));
    }


}
