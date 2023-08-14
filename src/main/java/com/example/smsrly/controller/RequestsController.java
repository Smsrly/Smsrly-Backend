package com.example.smsrly.controller;


import com.example.smsrly.utilities.Response;
import com.example.smsrly.service.RequestsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/request")
@RequiredArgsConstructor
public class RequestsController {

    private final RequestsService requestsService;

    @PostMapping("{id}")
    public ResponseEntity<Response> setUserRequest(@RequestHeader("Authorization") String authHeader,
                                                   @PathVariable("id") long realEstateId) {
        return ResponseEntity.ok(requestsService.setUserRequest(realEstateId, authHeader));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Response> deleteUserRequest(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("id") long realEstateId
    ) {
        return ResponseEntity.ok(requestsService.deleteUserRequest(authHeader, realEstateId));
    }

}
