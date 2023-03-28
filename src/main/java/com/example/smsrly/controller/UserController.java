package com.example.smsrly.controller;

import com.example.smsrly.entity.RealEstate;
import com.example.smsrly.entity.Request;
import com.example.smsrly.entity.Save;
import com.example.smsrly.response.Response;
import com.example.smsrly.response.UserResponse;
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

    @GetMapping
    public UserResponse getUser(@RequestHeader("Authorization") String authHeader) {
        return service.getUserInfo(authHeader);
    }

    @DeleteMapping
    public Response deleteUser(@RequestHeader("Authorization") String authHeader) {
        return service.deleteUser(authHeader);
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
        return service.updateUser(authHeader, null, firstName, lastName, password, phoneNumber, latitude, longitude, image);
    }

    @GetMapping(path = "/saves")
    public Set<Save> userSaves(@RequestHeader("Authorization") String authHeader) {
        return service.getUserSaves(authHeader);
    }

    @PostMapping(path = "/save/{realEstateId}")
    public Response saveRealEstate(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("realEstateId") int realEstateId
    ) {
        return service.saveRealEstate(authHeader, realEstateId);
    }

    @DeleteMapping(path = "/save/{realEstateId}")
    public Response deleteSaveRealEstate(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("realEstateId") int realEstateId
    ) {
        return service.deleteSaveRealEstate(authHeader, realEstateId);
    }

    @GetMapping(path = "/uploads")
    public List<RealEstate> getUserUploads(@RequestHeader("Authorization") String authHeader) {
        return service.getUserUploads(authHeader);
    }

    @GetMapping(path = "/requests")
    public Set<Request> getUserRequests(@RequestHeader("Authorization") String authHeader) {
        return service.getUserRequests(authHeader);
    }
}
