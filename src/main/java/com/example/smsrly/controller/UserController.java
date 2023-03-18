package com.example.smsrly.controller;

import com.example.smsrly.entity.RealEstate;
import com.example.smsrly.entity.User;
import com.example.smsrly.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
                           @RequestParam(required = false) String password,
                           @RequestParam(required = false) Optional<Long> phoneNumber,
                           @RequestParam(required = false) Optional<Double> latitude,
                           @RequestParam(required = false) Optional<Double> longitude,
                           @RequestParam(required = false) String image) {
        service.updateUser(userId, firstName, lastName, password, phoneNumber, latitude, longitude, image);
    }

    @GetMapping(path = "/saves/{userId}")
    public Set<RealEstate> userSaves(@PathVariable("userId") int userId) {
        return service.getUserSaves(userId);
    }

    @PostMapping("/{userId}/save/{realEstateId}")
    public void saveRealEstate(
            @PathVariable("userId") int userId,
            @PathVariable("realEstateId") int realEstateId
    ) {
        service.saveRealEstate(userId, realEstateId);
    }

    @DeleteMapping("/{userId}/save/{realEstateId}")
    public void deleteSaveRealEstate(
            @PathVariable("userId") int userId,
            @PathVariable("realEstateId") int realEstateId
    ) {
        service.deleteSaveRealEstate(userId, realEstateId);
    }

    @GetMapping("/uploads/{userId}")
    public List<RealEstate> getUserUploads(@PathVariable("userId") int userId) {
        return service.getUserUploads(userId);
    }
}
