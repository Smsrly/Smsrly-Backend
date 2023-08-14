package com.example.smsrly.controller;

import com.example.smsrly.auth.RegistrationRequest;
import com.example.smsrly.dto.RealEstateDTO;
import com.example.smsrly.dto.UserDTO;
import com.example.smsrly.dto.UserRealEstateDTO;
import com.example.smsrly.utilities.Response;
import com.example.smsrly.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserDTO> getUser(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(userService.getUserInfo(authHeader));
    }

    @DeleteMapping
    public ResponseEntity<Response> deleteUser(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(userService.deleteUser(authHeader));
    }

    @PutMapping
    public ResponseEntity<Response> updateUser(@RequestHeader("Authorization") String authHeader, @RequestBody @Valid RegistrationRequest request) {
        return ResponseEntity.ok(userService.updateUser(authHeader, request));
    }

    @GetMapping("uploads")
    public ResponseEntity<List<UserRealEstateDTO>> getUserUploads(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(userService.getUserUploads(authHeader));
    }

    @GetMapping("requests")
    public ResponseEntity<List<RealEstateDTO>> getUserRequests(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(userService.getUserRequests(authHeader));
    }

    @GetMapping("saves")
    public ResponseEntity<List<RealEstateDTO>> getUserSaves(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(userService.getUserSaves(authHeader));
    }

}
