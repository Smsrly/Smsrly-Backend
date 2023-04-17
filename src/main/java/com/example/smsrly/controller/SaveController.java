package com.example.smsrly.controller;


import com.example.smsrly.entity.Save;
import com.example.smsrly.response.RealEstateResponse;
import com.example.smsrly.response.Response;
import com.example.smsrly.service.SaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "/saves")
@RequiredArgsConstructor
public class SaveController {

    private final SaveService saveService;

    @GetMapping
    public List<RealEstateResponse> userSaves(@RequestHeader("Authorization") String authHeader) {
        return saveService.getUserSaves(authHeader);
    }

    @PostMapping
    public Response saveRealEstate(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("RealEstateId") int realEstateId
    ) {
        return saveService.saveRealEstate(authHeader, realEstateId);
    }

    @DeleteMapping
    public Response deleteSaveRealEstate(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("RealEstateId") int realEstateId
    ) {
        return saveService.deleteSaveRealEstate(authHeader, realEstateId);
    }
}
