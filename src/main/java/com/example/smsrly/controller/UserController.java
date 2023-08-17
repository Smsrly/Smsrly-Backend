package com.example.smsrly.controller;

import com.example.smsrly.auth.RegistrationRequest;
import com.example.smsrly.dto.UserDTO;
import com.example.smsrly.utilities.PagingResponse;
import com.example.smsrly.utilities.Response;
import com.example.smsrly.service.UserService;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserDTO> getUser(@RequestHeader("Authorization") String authHeader,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(userService.getUserInfo(authHeader, page, size));
    }

    @DeleteMapping
    public ResponseEntity<Response> deleteUser(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(userService.deleteUser(authHeader));
    }

    @PutMapping
    public ResponseEntity<Response> updateUser(@RequestHeader("Authorization") String authHeader,
                                               @RequestBody @Valid RegistrationRequest request) {
        return ResponseEntity.ok(userService.updateUser(authHeader, request));
    }

    @GetMapping("uploads")
    public ResponseEntity<PagingResponse> getUserUploads(@RequestHeader("Authorization") String authHeader,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(userService.getUserUploads(authHeader, page, size));
    }

    @GetMapping("requests")
    public ResponseEntity<PagingResponse> getUserRequests(@RequestHeader("Authorization") String authHeader,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(userService.getUserRequests(authHeader, page, size));
    }

    @GetMapping("saves")
    public ResponseEntity<PagingResponse> getUserSaves(@RequestHeader("Authorization") String authHeader,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(userService.getUserSaves(authHeader, page, size));
    }

}
