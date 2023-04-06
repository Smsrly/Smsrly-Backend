package com.example.smsrly.controller;


import com.example.smsrly.entity.Storage;
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
    public Response uploadImageToFIleSystem(@RequestParam("image") MultipartFile file, @RequestParam("email") String email) throws IOException {
        return storageService.uploadImage(file, email);
    }

    @GetMapping(path = "{fileName}")
    public ResponseEntity<?> downloadImageFromFileSystem(@PathVariable("fileName") String fileName) throws IOException {
        byte[] imageData = storageService.downloadImage(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);
    }

}
