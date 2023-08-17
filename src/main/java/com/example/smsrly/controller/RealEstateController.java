package com.example.smsrly.controller;

import com.example.smsrly.dao.SearchRequest;
import com.example.smsrly.utilities.PagingResponse;
import com.example.smsrly.utilities.RealEstateRequest;
import com.example.smsrly.utilities.Response;
import com.example.smsrly.service.RealEstateService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/real-estate")
@AllArgsConstructor
public class RealEstateController {

    private final RealEstateService realEstateService;

    @GetMapping
    public ResponseEntity<PagingResponse> getRealEstates(@RequestHeader("Authorization") String authHeader,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(realEstateService.getRealEstates(authHeader, page, size));
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
    public ResponseEntity<PagingResponse> getRealEstateRequests(@RequestHeader("Authorization") String authHeader,
                                                                @PathVariable(value = "id") long realEstateId,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(realEstateService.getRealEstateRequests(authHeader, realEstateId, page, size));
    }

    @GetMapping(path = "filter")
    public ResponseEntity<PagingResponse> filterRealEstate(@RequestHeader("Authorization") String authHeader,
                                                           @RequestBody SearchRequest searchRequest,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "5") int size
    ) {
        return ResponseEntity.ok(realEstateService.getFilteredRealEstate(authHeader, searchRequest, page, size));
    }
}
