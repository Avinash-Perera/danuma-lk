package com.avinash.danumalk.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.avinash.danumalk.common.PageResponse;

import java.util.UUID;

@RestController
@RequestMapping("users/admin")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService service;


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<UserResponse>> getAllUsersForAdmin(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok(service.getAllUsersForAdmin(page,size));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<String> deleteUser(@PathVariable UUID id, @AuthenticationPrincipal UserDetails userDetails) {
        service.deleteUser(id, userDetails);
        return ResponseEntity.ok("User deleted successfully");
    }

}
