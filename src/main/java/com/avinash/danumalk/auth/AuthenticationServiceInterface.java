package com.avinash.danumalk.auth;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthenticationServiceInterface {
     AuthenticationResponse registerAdmin(RegisterRequest request);

    boolean register(RegisterRequest request) throws MessagingException;

    void resendActivationCode(String email) throws MessagingException;

    void activateAccount(String token) throws MessagingException;

    AuthenticationResponse authenticate(AuthenticationRequest request);

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
