package com.example.smsrly.controller;


import com.example.smsrly.utilities.Response;
import com.example.smsrly.service.SaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/save")
@RequiredArgsConstructor
public class SaveController {

    private final SaveService saveService;


    @PostMapping("{id}")
    public ResponseEntity<Response> saveRealEstate(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("id") long realEstateId
    ) {
        return ResponseEntity.ok(saveService.saveRealEstate(authHeader, realEstateId));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Response> deleteSaveRealEstate(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("id") long realEstateId
    ) {
        return ResponseEntity.ok(saveService.deleteSaveRealEstate(authHeader, realEstateId));
    }
}
