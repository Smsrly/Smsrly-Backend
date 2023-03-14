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

    private final AuthenticationService service;

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        return service.register(request);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping(path = "confirm")
    public AuthenticationResponse confirm(@RequestParam("code") String code) {
        return service.confirmCode(code);
    }

}
