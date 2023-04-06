package com.example.smsrly.service;

import com.example.smsrly.entity.Storage;
import com.example.smsrly.entity.User;
import com.example.smsrly.repository.UserRepository;
import com.example.smsrly.repository.StorageRepository;
import com.example.smsrly.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StorageService {

    private static final String USER_FOLDER_PATH = "C:\\Users\\Youssef\\IdeaProjects\\smsrly\\src\\main\\Images\\User\\";
    private static final String REAL_ESTATE_FOLDER_PATH = "C:\\Users\\Youssef\\IdeaProjects\\smsrly\\src\\main\\Images\\User\\";
    private final StorageRepository storageRepository;
    private final UserRepository userRepository;

    public static String extractUserNameFromEmail(String email) {
        String userName = "";
        for (int i = 0; i < email.length(); i++) {
            if (email.charAt(i) == '@') return userName;
            userName += email.charAt(i);
        }
        return userName;
    }

    public Response uploadImage(MultipartFile file, String email) throws IOException {

        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new IllegalStateException("user with email " + email + " not exists"));

        String fileExtension = com.google.common.io.Files.getFileExtension(Objects.requireNonNull(file.getOriginalFilename()));
        String renameFile = extractUserNameFromEmail(email) + '.' + fileExtension;
        String filePath = USER_FOLDER_PATH + renameFile;

        if (storageRepository.findImageByName(renameFile).isPresent())
            return Response.builder().message("image is already uploaded").build();

        storageRepository.save(Storage.builder()
                .name(renameFile)
                .type(file.getContentType())
                .path(filePath).build());

        user.setImageURL("http://localhost:8080/image/" + renameFile);

        userRepository.save(user);

        file.transferTo(new File(filePath));

        return Response.builder().message("image uploaded").build();
    }

    public byte[] downloadImage(String fileName) throws IOException {
        Storage fileData = storageRepository.findImageByName(fileName).orElseThrow(() -> new IllegalArgumentException("image with name " + fileName + " not found"));
        String filePath = fileData.getPath();
        return Files.readAllBytes(new File(filePath).toPath());
    }

}
