package com.example.smsrly.controller;

import com.example.smsrly.entity.RealEstate;
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
    public List<RealEstate> realEstates() {
        return service.getRealEstates();
    }


    @GetMapping(path = "{realEstateId}")
    public Optional<RealEstate> realEstate(@PathVariable("realEstateId") int realEstateId) {
        return service.getRealEstate(realEstateId);
    }

    @PostMapping
    public void addRealEstate(@RequestBody RealEstate realEstate) {
        service.addRealEstate(realEstate);
    }
    @DeleteMapping(path = "{realEstateId}")
    public void deleteRealEstate(@PathVariable("realEstateId") int realEstateId) {
        service.deleteRealEstate(realEstateId);
    }

    @PutMapping(path = "{realEstateId}")
    public void updateRealEstate(@PathVariable("realEstateId") int realEstateId,
                           @RequestParam(required = false) String title,
                           @RequestParam(required = false) String description,
                           @RequestParam(required = false) double area,
                           @RequestParam(required = false) int floorNumber,
                           @RequestParam(required = false) int bathroomNumber,
                           @RequestParam(required = false) int roomNumber,
                           @RequestParam(required = false) double price,
                           @RequestParam(required = false) double latitude,
                           @RequestParam(required = false) double longitude,
                           @RequestParam(required = false) String image) {
        service.updateRealEstate(realEstateId, title, description, area, floorNumber, bathroomNumber, roomNumber, price, latitude, longitude, image);
    }

}
