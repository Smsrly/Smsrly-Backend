package com.example.smsrly.controller;

import com.example.smsrly.dto.RealEstateDTO;
import com.example.smsrly.dto.UserRequestDTO;
import com.example.smsrly.utilities.RealEstateRequest;
import com.example.smsrly.utilities.Response;
import com.example.smsrly.service.RealEstateService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/real-estate")
@AllArgsConstructor
public class RealEstateController {

    private final RealEstateService realEstateService;

    @GetMapping
    public ResponseEntity<List<RealEstateDTO>> getRealEstates(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(realEstateService.getRealEstates(authHeader));
    }

    @PostMapping
    public ResponseEntity<Response> uploadRealEstate(@RequestHeader("Authorization") String authHeader,
                                                     @RequestBody @Valid RealEstateRequest realEstate) {
        return ResponseEntity.ok(realEstateService.uploadRealEstate(realEstate, authHeader));
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Response> deleteRealEstate(@RequestHeader("Authorization") String authHeader,
                                                     @PathVariable("id") long realEstateId) {
        return ResponseEntity.ok(realEstateService.deleteRealEstate(authHeader, realEstateId));
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<Response> updateRealEstate(@RequestHeader("Authorization") String authHeader,
                                                     @PathVariable("id") long realEstateId,
                                                     @RequestBody @Valid RealEstateRequest request) {
        return ResponseEntity.ok(realEstateService.updateRealEstate(authHeader, realEstateId, request));
    }

    @GetMapping(path = "{id}/requests")
    public ResponseEntity<List<UserRequestDTO>> getRealEstateRequests(@RequestHeader("Authorization") String authHeader, @PathVariable(value = "id") long realEstateId) {
        return ResponseEntity.ok(realEstateService.getRealEstateRequests(authHeader,realEstateId));
    }

}
