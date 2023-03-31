package com.example.smsrly.controller;

import com.example.smsrly.auth.AuthenticationRequest;
import com.example.smsrly.auth.AuthenticationResponse;
import com.example.smsrly.auth.AuthorizationRequest;
import com.example.smsrly.auth.RegisterRequest;
import com.example.smsrly.response.Response;
import com.example.smsrly.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping(path = "/register")
    public Response register(@RequestBody @Valid RegisterRequest request) {
        return authenticationService.register(request);
    }

    @PostMapping(path = "/authentication")
    public AuthenticationResponse authenticate(@RequestBody @Valid AuthenticationRequest request) {
        return authenticationService.authenticate(request);
    }

    @PostMapping(path = "/authorization")
    public AuthenticationResponse authorization(@RequestBody @Valid AuthorizationRequest request) {
        return authenticationService.authorization(request);
    }

    @GetMapping(path = "/confirmEmail")
    public AuthenticationResponse confirmEmail(@RequestParam("email") String email,
                                               @RequestParam("code") int code) {
        return authenticationService.emailConfirmationCode(email, code);
    }

    @GetMapping(path = "/resetPassword")
    public Response resetPassword(@RequestParam("email") String userEmail) {
        return authenticationService.resetPassword(userEmail);
    }

    @GetMapping(path = "/confirmPassword")
    public AuthenticationResponse confirmPassword(@RequestParam("email") String email,
                                                  @RequestParam("code") int code) {
        return authenticationService.passwordConfirmationCode(email, code);
    }

    @PutMapping(path = "/newPassword")
    public Response updateUserPassword(@RequestHeader("Authorization") String authHeader,
                                       @RequestParam("password") String password) {
        return authenticationService.updateUserPassword(authHeader, password);
    }


}
