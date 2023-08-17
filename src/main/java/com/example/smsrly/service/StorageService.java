package com.example.smsrly.service;

import com.example.smsrly.entity.RealEstate;
import com.example.smsrly.entity.RealEstateImage;
import com.example.smsrly.entity.Storage;
import com.example.smsrly.entity.User;
import com.example.smsrly.exception.InputException;
import com.example.smsrly.repository.RealEstateImagesRepository;
import com.example.smsrly.repository.RealEstateRepository;
import com.example.smsrly.repository.UserRepository;
import com.example.smsrly.repository.StorageRepository;
import com.example.smsrly.utilities.Response;
import com.example.smsrly.utilities.Util;
import jakarta.transaction.Transactional;
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
    private static final String BASE_URL = "http://127.0.0.1:8080/";
    private final StorageRepository storageRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final RealEstateRepository realEstateRepository;
    private final RealEstateImagesRepository realEstateImagesRepository;
    private final Util util;


    public Response uploadImage(String authHeader, MultipartFile file) throws IOException {
        User user = userService.getUser(authHeader);

        // Check if the file content type is an image type
        if (!file.getContentType().startsWith("image/")) {
            throw new InputException(util.getMessage("image.error.format"));
        }

        String fileExtension = com.google.common.io.Files.getFileExtension(Objects.requireNonNull(file.getOriginalFilename()));
        String renameFile = util.extractUserNameFromEmail(user.getEmail()) + '.' + fileExtension;
        String filePath = USER_FOLDER_PATH + renameFile;

        if (storageRepository.findImageByFilename(renameFile).isPresent()) {
            throw new InputException(util.getMessage("account.image.exists"));
        }

        storageRepository.save(Storage.builder()
                .filename(renameFile)
                .filetype(file.getContentType())
                .filepath(filePath)
                .build());

        user.setImageURL(BASE_URL + "auth/image/" + renameFile);

        userRepository.save(user);

        file.transferTo(new File(filePath));

        return Response.builder().message(util.getMessage("account.image.uploaded")).build();
    }


    public Response uploadImage(String authHeader, MultipartFile[] files, long realEstateId) throws IOException {

        User user = userService.getUser(authHeader);

        RealEstate realEstate = realEstateRepository.findById(realEstateId).orElseThrow(() -> new InputException("real estate with id " + realEstateId + " not exists"));

        if (user != realEstate.getUser()) {
            throw new InputException(util.getMessage("real.estate.not.owner"));
        }

        int numberOfUploadedRealEstateImages = realEstateImagesRepository.findRealEstateImagesByRealEstate(realEstate).size();

        if ((numberOfUploadedRealEstateImages + files.length) > 20) {
            throw new InputException(util.getMessage("real.estate.upload.image.limit"));
        }

        for (MultipartFile file : files) {

            String fileExtension = com.google.common.io.Files.getFileExtension(Objects.requireNonNull(file.getOriginalFilename()));

            int imageNumber = realEstateImagesRepository.findLastIdNumber() == null ? 0 : realEstateImagesRepository.findLastIdNumber();

            // Check if the file content type is an image type
            if (!file.getContentType().startsWith("image/")) {
                throw new InputException(file.getOriginalFilename() + ": invalid file format. only image files are allowed.");
            }

            String renameFile = util.extractUserNameFromEmail(realEstate.getUser().getEmail()) + '_' + realEstateId + '_' + realEstate.getTitle().replaceAll("\\s", "_") + '_' + ++imageNumber + '.' + fileExtension;
            String filePath = REAL_ESTATE_FOLDER_PATH + renameFile;

            storageRepository.save(Storage.builder()
                    .filename(renameFile)
                    .filetype(file.getContentType())
                    .filepath(filePath)
                    .build());

            realEstateImagesRepository.save(new RealEstateImage(renameFile, BASE_URL + "auth/image/" + renameFile, realEstate));

            file.transferTo(new File(filePath));

        }

        return Response.builder().message(util.getMessage("account.image.uploaded")).build();
    }

    public byte[] downloadImage(String fileName) throws IOException {
        Storage fileData = storageRepository.findImageByFilename(fileName).orElseThrow(() -> new InputException("image with file name " + fileName + " not found"));
        String filePath = fileData.getFilepath();
        return Files.readAllBytes(new File(filePath).toPath());
    }

    @Transactional
    public Response deleteImage(String authHeader, String filename, String deleteType) {

        User user = userService.getUser(authHeader);
        Storage storage = storageRepository.findImageByFilename(filename).orElseThrow(() -> new InputException("image with file name " + filename + " not found"));
        String imagePath = storageRepository.findFilepathByFileName(filename);
        File file = new File(imagePath);

        if (deleteType.equals("user")) {
            if (user.getImageURL() == null || !user.getImageURL().contains(filename)) {
                throw new InputException("image with file name " + filename + " not found");
            }
            user.setImageURL(null);
        } else if (deleteType.equals("real-estate")) {
            if (!filename.contains(util.extractUserNameFromEmail(user.getEmail()))) {
                throw new InputException(util.getMessage("real.estate.not.owner"));
            }
            RealEstateImage realEstateImage = realEstateImagesRepository.findRealEstateImageByFilename(filename).orElseThrow(() -> new InputException("image with file name " + filename + " not found"));
            System.out.println(realEstateImage.getFilename());
            realEstateImagesRepository.delete(realEstateImage);
        } else {
            throw new InputException(util.getMessage("image.type.not.exists"));
        }

        if (file.exists()) {
            file.delete();
        }
        storageRepository.delete(storage);

        return Response.builder().message(util.getMessage("account.image.deleted")).build();
    }
}
