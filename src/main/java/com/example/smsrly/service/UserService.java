package com.example.smsrly.service;

import com.example.smsrly.entity.RealEstate;
import com.example.smsrly.entity.Request;
import com.example.smsrly.entity.Save;
import com.example.smsrly.entity.User;
import com.example.smsrly.repository.RealEstateRepository;
import com.example.smsrly.repository.SaveRepository;
import com.example.smsrly.repository.UserRepository;
import com.example.smsrly.response.Response;
import com.example.smsrly.response.UserResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RealEstateRepository realEstateRepository;
    private final JwtService jwtService;
    private final SaveRepository saveRepository;
    private final ValidatingService validatingService;

    public String extractEmailFromToken(String authHeader) {
        String token = authHeader.substring(7);
        return jwtService.extractEmail(token);
    }

    public int getUserId(String authHeader) {
        String userEmail = extractEmailFromToken(authHeader);
        User user = userRepository.findUserByEmail(userEmail).orElseThrow(() -> new IllegalStateException("user with email " + userEmail + " not exists"));
        return user.getId();
    }

    public User getUser(String authHeader) {
        String userEmail = extractEmailFromToken(authHeader);
        return userRepository.findUserByEmail(userEmail).orElseThrow(() -> new IllegalStateException("user with email " + userEmail + " not exists"));
    }

    public UserResponse getUserInfo(String authHeader) {
        User user = getUser(authHeader);
        return UserResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .latitude(user.getLatitude())
                .longitude(user.getLongitude())
                //.image(user.getImageURL())
                .build();
    }

    public Response deleteUser(String authHeader) {
        userRepository.deleteById(getUserId(authHeader));
        return Response.builder().message("user deleted").build();
    }

    @Transactional
    public Response updateUser(String authHeader, String email, String firstName, String lastName, String password, Optional<Long> phoneNumber, Optional<Double> latitude, Optional<Double> longitude, String image) {

        User user = authHeader != null ? getUser(authHeader) : userRepository.findUserByEmail(email).orElseThrow(() -> new IllegalStateException("User not found"));

        if (firstName != null && !firstName.equals(user.getFirstName())) {
            String validationMessage = validatingService.validating(firstName, null, null, null, 0, 0, 0, null, 1);
            if (validationMessage != "validated") {
                return Response.builder().message(validationMessage).build();
            }
            user.setFirstName(firstName.replaceAll("\\s", ""));
        }

        if (lastName != null && !lastName.equals(user.getLastName())) {
            String validationMessage = validatingService.validating(null, lastName, null, null, 0, 0, 0, null, 2);
            if (validationMessage != "validated") {
                return Response.builder().message(validationMessage).build();
            }
            user.setLastName(lastName.replaceAll("\\s", ""));
        }

        if (password != null && !passwordEncoder.matches(password, user.getPassword())) {

            String validationMessage = validatingService.validating(null, null, null, password, 0, 0, 0, null, 4);

            if (validationMessage != "validated") {
                return Response.builder().message(validationMessage).build();
            }

            user.setPassword(passwordEncoder.encode(password));
        }

        if (phoneNumber.isPresent()) {
            long phoneNum = phoneNumber.get();

            String validationMessage = validatingService.validating(null, null, null, null, phoneNum, 0, 0, null, 5);
            if (validationMessage != "validated") {
                return Response.builder().message(validationMessage).build();
            }

            if (phoneNum != user.getPhoneNumber()) {
                user.setPhoneNumber(phoneNum);
            }
        }

        if (latitude.isPresent() && longitude.isPresent()) {
            double lat = latitude.get();
            double lon = longitude.get();

            if (!(lat == user.getLatitude()) || !(lon == user.getLongitude())) {

                String validationMessage = validatingService.validating(null, null, null, null, 0, lat, lon, null, 6);
                if (validationMessage != "validated") {
                    return Response.builder().message(validationMessage).build();
                }

                user.setLatitude(lat);
                user.setLongitude(lon);
            }

        }

        return Response.builder().message("updated").build();
    }

    public List<RealEstate> getUserUploads(String authHeader) {
        User user = getUser(authHeader);
        return user.getUploads();
    }

}
