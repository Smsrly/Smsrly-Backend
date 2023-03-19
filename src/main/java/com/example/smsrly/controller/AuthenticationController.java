package com.example.smsrly.controller;

import com.example.smsrly.auth.AuthenticationRequest;
import com.example.smsrly.auth.AuthenticationResponse;
import com.example.smsrly.auth.RegisterRequest;
import com.example.smsrly.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        return authenticationService.register(request);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @GetMapping(path = "/confirmEmail")
    public AuthenticationResponse confirmEmail(@RequestParam("code") int code) {
        return authenticationService.emailConfirmationCode(code);
    }

    @GetMapping(path = "/resetPassword")
    public String resetPassword(@RequestParam("email") String userEmail) {
        return authenticationService.resetPassword(userEmail);
    }

    @GetMapping(path = "/confirmPassword")
    public AuthenticationResponse confirmPassword(@RequestParam("code") int code) {
        return authenticationService.passwordConfirmationCode(code);
    }

    @PutMapping(path = "/newPassword")
    public String updateUserPassword(@RequestHeader("Authorization") String authHeader,
                                     @RequestParam("password") String password) {
        return authenticationService.updateUserPassword(authHeader, password);
    }


}
