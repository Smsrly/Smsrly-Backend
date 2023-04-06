package com.example.smsrly.controller;


import com.example.smsrly.response.Response;
import com.example.smsrly.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(path = "/image")
@RequiredArgsConstructor
public class ImageController {

    private final StorageService storageService;

    @PostMapping
    public Response upload(@RequestParam("image") MultipartFile file, @RequestParam(value = "email") String email) throws IOException {
        return storageService.uploadImage(file, email);
    }

    @PostMapping(path = "realEstate")
    public Response upload(@RequestParam("image") MultipartFile file, @RequestParam(value = "RealEstateId") int realEstateId) throws IOException {
        return storageService.uploadImage(file, realEstateId);
    }

    @GetMapping(path = "{fileName}")
    public ResponseEntity<?> download(@PathVariable("fileName") String fileName) throws IOException {
        byte[] imageData = storageService.downloadImage(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);
    }

}
