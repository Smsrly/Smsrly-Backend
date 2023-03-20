package com.example.smsrly.controller;

import com.example.smsrly.entity.RealEstate;
import com.example.smsrly.entity.Request;
import com.example.smsrly.service.RealEstateService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/realEstate")
@AllArgsConstructor
public class RealEstateController {

    private final RealEstateService service;


    @GetMapping
    public List<RealEstate> realEstates(@RequestHeader("Authorization") String authHeader) {
        return service.getRealEstates(authHeader);
    }


    @GetMapping(path = "/{realEstateId}")
    public Optional<RealEstate> realEstate(@PathVariable("realEstateId") int realEstateId) {
        return service.getRealEstate(realEstateId);
    }

    @PostMapping
    public void addRealEstate(@RequestHeader("Authorization") String authHeader,
                              @RequestBody RealEstate realEstate) {
        service.addRealEstate(realEstate, authHeader);
    }

    @DeleteMapping(path = "{realEstateId}")
    public String deleteRealEstate(@RequestHeader("Authorization") String authHeader,
                                   @PathVariable("realEstateId") int realEstateId) {
        return service.deleteRealEstate(authHeader, realEstateId);
    }

    @PutMapping(path = "{realEstateId}")
    public String updateRealEstate(@RequestHeader("Authorization") String authHeader,
                                 @PathVariable("realEstateId") int realEstateId,
                                 @RequestParam(required = false) String title,
                                 @RequestParam(required = false) String description,
                                 @RequestParam(required = false) Optional<Double> area,
                                 @RequestParam(required = false) Optional<Integer> floorNumber,
                                 @RequestParam(required = false) Optional<Integer> bathroomNumber,
                                 @RequestParam(required = false) Optional<Integer> roomNumber,
                                 @RequestParam(required = false) Optional<Double> price,
                                 @RequestParam(required = false) Optional<Double> latitude,
                                 @RequestParam(required = false) Optional<Double> longitude,
                                 @RequestParam(required = false) String image) {
        return service.updateRealEstate(authHeader, realEstateId, title, description, area, floorNumber, bathroomNumber, roomNumber, price, latitude, longitude, image);
    }


    @PostMapping(path = "/request/{realEstateId}")
    public String setUserRequest(@RequestHeader("Authorization") String authHeader,
                               @PathVariable("realEstateId") int realEstateId) {
        return service.addRequest(realEstateId, authHeader);
    }

    @GetMapping(path = "/requestedBy/{realEstateId}")
    public List<Request> getRealEstateRequests(@PathVariable(value = "realEstateId") int realEstateId) {
        return service.getRealEstateRequests(realEstateId);
    }
}
