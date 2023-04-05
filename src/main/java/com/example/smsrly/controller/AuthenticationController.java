package com.example.smsrly.controller;

import com.example.smsrly.auth.AuthenticationRequest;
import com.example.smsrly.auth.AuthenticationResponse;
import com.example.smsrly.auth.AuthorizationRequest;
import com.example.smsrly.auth.RegisterRequest;
import com.example.smsrly.response.Response;
import com.example.smsrly.service.AuthenticationService;
import com.example.smsrly.service.StorageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final StorageService storageService;

    @PostMapping(path = "/register")
    public Response register(@RequestBody @Valid RegisterRequest request) throws IOException {
        return authenticationService.register(request);
    }

    @PostMapping(path = "/image")
    public ResponseEntity<?> uploadImageToFIleSystem(@RequestParam("image") MultipartFile file, @RequestParam("email") String email) throws IOException {
        String uploadImage = storageService.uploadImage(file, email);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImage);
    }

    @GetMapping("/image/{fileName}")
    public ResponseEntity<?> downloadImageFromFileSystem(@PathVariable String fileName) throws IOException {
        byte[] imageData = storageService.downloadImage(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);
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
