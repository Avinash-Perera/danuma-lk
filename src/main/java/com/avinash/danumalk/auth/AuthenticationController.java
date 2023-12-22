package com.avinash.danumalk.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    /**
     * Handles the POST request to register a new user.
     *
     * @param  request  the register request object containing user details
     * @param  result   the binding result object for request validation
     * @return          a response entity containing the result of the registration process
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request, BindingResult result) {
        if (result.hasErrors()) {
            // Validation failed, return validation errors to the client
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildValidationErrors(result));
        }

        return ResponseEntity.ok(service.register(request));
    }

    // Helper method to build validation error response
    private Map<String, String> buildValidationErrors(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(fieldError ->
                errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
        return errors;
    }

    /**
     * Authenticates a user by processing an authentication request.
     *
     * @param  request    the authentication request containing user credentials
     * @return            the response entity containing the authentication response
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    /**
     * Refreshes the token for the given request and response.
     *
     * @param  request   the HttpServletRequest object
     * @param  response  the HttpServletResponse object
     * @return           void
     * @throws IOException if an I/O error occurs
     */
    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }


}