package com.example.smsrly.controller;

import com.example.smsrly.entity.RealEstate;
import com.example.smsrly.entity.User;
import com.example.smsrly.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/user")
@AllArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping(path = "{userId}")
    public Optional<User> user(@PathVariable("userId") int userId) {
        return service.getUser(userId);
    }

    @DeleteMapping(path = "{userId}")
    public void deleteUser(@PathVariable("userId") int userId) {
        service.deleteUser(userId);
    }

    @PutMapping(path = "{userId}")
    public void updateUser(@PathVariable("userId") int userId,
                           @RequestParam(required = false) String firstName,
                           @RequestParam(required = false) String lastName,
                           @RequestParam(required = false) String email,
                           @RequestParam(required = false) String password,
                           @RequestParam(required = false) long phoneNumber,
                           @RequestParam(required = false) double latitude,
                           @RequestParam(required = false) double longitude,
                           @RequestParam(required = false) String image) {
        service.updateUser(userId, firstName, lastName, email, password, phoneNumber, latitude, longitude, image);
    }


    @GetMapping(path = "/uploads/{userId}")
    public List<RealEstate> userUploads(@PathVariable("userId") int userId) {
        return service.getUserUploads(userId);
    }
}
