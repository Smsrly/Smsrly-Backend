package com.example.smsrly.controller;


import com.example.smsrly.utilities.Response;
import com.example.smsrly.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(path = "/image")
@RequiredArgsConstructor
public class ImageController {

    private final StorageService storageService;

    @PostMapping("user")
    public ResponseEntity<Response> upload(@RequestHeader("Authorization") String authHeader,
                                           @RequestParam("image") MultipartFile file) throws IOException {
        return ResponseEntity.ok(storageService.uploadImage(authHeader, file));
    }

    @PostMapping(path = "real-estate/{id}")
    public ResponseEntity<Response> upload(@RequestHeader("Authorization") String authHeader,
                                           @RequestParam("image") MultipartFile[] files,
                                           @PathVariable(value = "id") long realEstateId) throws IOException {
        return ResponseEntity.ok(storageService.uploadImage(authHeader, files, realEstateId));
    }

    @DeleteMapping(path = "/{deleteType}/{fileName}")
    public ResponseEntity<Response> delete(@RequestHeader("Authorization") String authHeader,
                                           @PathVariable("deleteType") String deleteType,
                                           @PathVariable("fileName") String fileName) {
        return ResponseEntity.ok(storageService.deleteImage(authHeader, fileName, deleteType));
    }

}
