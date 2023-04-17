package com.example.smsrly.controller;


import com.example.smsrly.entity.Request;
import com.example.smsrly.response.RealEstateResponse;
import com.example.smsrly.response.Response;
import com.example.smsrly.service.RequestsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class RequestsController {

    private final RequestsService requestsService;

    @GetMapping
    public List<RealEstateResponse> getUserRequests(@RequestHeader("Authorization") String authHeader) {
        return requestsService.getUserRequests(authHeader);
    }

    @PostMapping
    public Response setUserRequest(@RequestHeader("Authorization") String authHeader,
                                   @RequestParam("RealEstateId") int realEstateId) {
        return requestsService.addRequest(realEstateId, authHeader);
    }

    @DeleteMapping
    public Response deleteUserRequest(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("RealEstateId") int realEstateId
    ) {
        return requestsService.deleteUserRequest(authHeader, realEstateId);
    }

    @GetMapping(path = "/requestedBy")
    public List<Request> getRealEstateRequests(@RequestParam(value = "RealEstateId") int realEstateId) {
        return requestsService.getRealEstateRequests(realEstateId);
    }


}
