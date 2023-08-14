package com.example.smsrly.controller;

import com.example.smsrly.auth.*;
import com.example.smsrly.service.StorageService;
import com.example.smsrly.utilities.Response;
import com.example.smsrly.service.AuthenticationService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final StorageService storageService;

    @GetMapping(path = "/verify-email")
    public ResponseEntity<Response> verifyEmail(@RequestBody @Valid OTPRequest request) throws MessagingException {
        return ResponseEntity.ok(authenticationService.verifyEmail(request));
    }

    @PostMapping(path = "/signup")
    public ResponseEntity<AuthenticationResponse> signup(@RequestBody @Valid RegistrationRequest request, @RequestParam("otp") int otp) {
        return ResponseEntity.ok(authenticationService.signup(request, otp));
    }

    @PostMapping(path = "/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @PostMapping(path = "/third-party-auth")
    public ResponseEntity<AuthenticationResponse> authorization(@RequestBody @Valid AuthorizationRequest request) {
        return ResponseEntity.ok(authenticationService.authorization(request));
    }

    @GetMapping(path = "/reset-password")
    public ResponseEntity<Response> resetPassword(@RequestParam("email") String email) throws MessagingException {
        return ResponseEntity.ok(authenticationService.resetPassword(email));
    }

    @GetMapping(path = "/confirm-password")
    public ResponseEntity<AuthenticationResponse> confirmPassword(@RequestParam("email") String email,
                                                                  @RequestParam("code") int code) {
        return ResponseEntity.ok(authenticationService.passwordConfirmationCode(email, code));
    }

    @PutMapping(path = "/new-password")
    public ResponseEntity<Response> updateUserPassword(@RequestHeader("Authorization") String authHeader,
                                                       @RequestParam("password") String password) {
        return ResponseEntity.ok(authenticationService.updateUserPassword(authHeader, password));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authenticationService.refreshToken(request, response);
    }

    @GetMapping(path = "/image/{fileName}")
    public ResponseEntity<?> download(@PathVariable("fileName") String fileName) throws IOException {
        byte[] imageData = storageService.downloadImage(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);
    }
}
