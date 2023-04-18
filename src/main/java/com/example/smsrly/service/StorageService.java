package com.example.smsrly.service;

import com.example.smsrly.entity.RealEstate;
import com.example.smsrly.entity.RealEstateImages;
import com.example.smsrly.entity.Storage;
import com.example.smsrly.entity.User;
import com.example.smsrly.repository.RealEstateImagesRepository;
import com.example.smsrly.repository.RealEstateRepository;
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

    private static final String PROJECT_PATH = "C:\\Users\\Youssef\\IdeaProjects\\smsrly\\"; // add your project path here (you can write 'pwd' command at terminal to get you path so copy and paste it here)
    private static final String USER_FOLDER_PATH = PROJECT_PATH + "src\\main\\Images\\User\\";
    private static final String REAL_ESTATE_FOLDER_PATH = PROJECT_PATH + "src\\main\\Images\\RealEstate\\";
    private static final String BASE_URL = "http://192.168.1.11:8080/";
    private final StorageRepository storageRepository;
    private final UserRepository userRepository;
    private final RealEstateRepository realEstateRepository;
    private final RealEstateImagesRepository realEstateImagesRepository;

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

        user.setImageURL(BASE_URL + "image/" + renameFile);

        userRepository.save(user);

        file.transferTo(new File(filePath));

        return Response.builder().message("image uploaded").build();
    }


    public Response uploadImage(MultipartFile[] files, int realEstateId) throws IOException {

        RealEstate realEstate = realEstateRepository.findById(realEstateId).orElseThrow(() -> new IllegalStateException("real estate with id " + realEstateId + " not exists"));

        for (MultipartFile file : files) {

            int numberOfUploadedRealEstates = realEstateImagesRepository.findByRealEstateId(realEstateId).size();
            String fileExtension = com.google.common.io.Files.getFileExtension(Objects.requireNonNull(file.getOriginalFilename()));
            String renameFile = extractUserNameFromEmail(realEstate.getUser().getEmail()) + '_' + realEstateId + '_' + realEstate.getTitle().replaceAll("\\s", "_") + '_' + ++numberOfUploadedRealEstates + '.' + fileExtension;
            String filePath = REAL_ESTATE_FOLDER_PATH + renameFile;

            storageRepository.save(Storage.builder()
                    .name(renameFile)
                    .type(file.getContentType())
                    .path(filePath)
                    .build());

            realEstateImagesRepository.save(new RealEstateImages(renameFile, BASE_URL + "image/" + renameFile, realEstate));

            file.transferTo(new File(filePath));

        }

        return Response.builder().message("image uploaded").build();
    }

    public byte[] downloadImage(String fileName) throws IOException {
        Storage fileData = storageRepository.findImageByName(fileName).orElseThrow(() -> new IllegalStateException("image with name " + fileName + " not found"));
        String filePath = fileData.getPath();
        return Files.readAllBytes(new File(filePath).toPath());
    }

//    public Response deleteImage(String fileName, String deleteType) {
//        Storage storage = storageRepository.findImageByName(fileName).orElseThrow(() -> new IllegalStateException("image with name " + fileName + " not found"));
//        String imagePath = storageRepository.findImagePathByImageName(fileName);
//        File file = new File(imagePath);
//        if (file.exists()) {
//            file.delete();
//        }
//        storageRepository.delete(storage);
//
//        if (deleteType.equals("user")) {
//            userRepository.deleteImageURL(BASE_URL + "image/" + fileName);
//        } else {
//            RealEstateImages realEstateImages = realEstateImagesRepository.findByImageURL(BASE_URL + "image/" + fileName).orElseThrow(() -> new IllegalStateException("image with name " + fileName + " not found"));
//            realEstateImagesRepository.delete(realEstateImages);
//        }
//
//        return Response.builder().message("image deleted").build();
//    }
}
