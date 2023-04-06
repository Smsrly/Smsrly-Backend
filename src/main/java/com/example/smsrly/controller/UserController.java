package com.example.smsrly.controller;

import com.example.smsrly.entity.RealEstate;
import com.example.smsrly.response.Response;
import com.example.smsrly.response.UserResponse;
import com.example.smsrly.service.StorageService;
import com.example.smsrly.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public UserResponse getUser(@RequestHeader("Authorization") String authHeader) {
        return userService.getUserInfo(authHeader);
    }

    @DeleteMapping
    public Response deleteUser(@RequestHeader("Authorization") String authHeader) {
        return userService.deleteUser(authHeader);
    }

    @PutMapping
    public Response updateUser(@RequestHeader("Authorization") String authHeader,
                               @RequestParam(required = false) String firstName,
                               @RequestParam(required = false) String lastName,
                               @RequestParam(required = false) String password,
                               @RequestParam(required = false) Optional<Long> phoneNumber,
                               @RequestParam(required = false) Optional<Double> latitude,
                               @RequestParam(required = false) Optional<Double> longitude,
                               @RequestParam(required = false) String image) {
        return userService.updateUser(authHeader, null, firstName, lastName, password, phoneNumber, latitude, longitude, image);
    }


    @GetMapping(path = "/uploads")
    public List<RealEstate> getUserUploads(@RequestHeader("Authorization") String authHeader) {
        return userService.getUserUploads(authHeader);
    }

}
