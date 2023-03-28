package com.example.smsrly.service;

import com.example.smsrly.config.JwtService;
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
                .image(user.getImage())
                .build();
    }

    public Response deleteUser(String authHeader) {
        userRepository.deleteById(getUserId(authHeader));
        return Response.builder().message("user deleted").build();
    }

    @Transactional
    public Response updateUser(String authHeader, String email, String firstName, String lastName, String password, Optional<Long> phoneNumber, Optional<Double> latitude, Optional<Double> longitude, String image) {

        if (email == null) {
            return Response.builder().message("User not found").build();
        }

        User user = authHeader != null ? getUser(authHeader) : userRepository.findUserByEmail(email).orElseThrow(() -> new IllegalStateException("User not found"));


        if (firstName != null && firstName.length() > 0 && !firstName.equals(user.getFirstName())) {
            user.setFirstName(firstName);
        }

        if (lastName != null && lastName.length() > 0 && !lastName.equals(user.getLastName())) {
            user.setLastName(lastName);
        }

//        if (email != null && email.length() > 0 && !email.equals(user.getEmail())) {
//            Optional<User> userEmail = userRepository.findUserByEmail(email);
//            if (userEmail.isPresent()) {
//                throw new IllegalStateException("email is already inserted into DB from another user");
//            }
//            user.setEmail(email);
//        }

        if (password != null && password.length() > 0 && !passwordEncoder.matches(password, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(password));
        }

        if (phoneNumber.isPresent()) {
            long phoneNum = phoneNumber.get();

            if (phoneNum > 9999 && phoneNum != user.getPhoneNumber()) {
                user.setPhoneNumber(phoneNum);
            }
        }

        if (latitude.isPresent()) {
            double lat = latitude.get();
            if (lat > 0 && !(lat == user.getLatitude())) {
                user.setLongitude(lat);
            }
        }

        if (longitude.isPresent()) {
            double lon = longitude.get();
            if (lon > 0 && !(lon == user.getLongitude())) {
                user.setLatitude(lon);
            }
        }

        if (image != null && image.length() > 0 && !image.equals(user.getImage())) {
            user.setImage(image);
        }

        return Response.builder().message("updated").build();
    }

    public Response saveRealEstate(String authHeader, int realEstateId) {

        User user = getUser(authHeader);

        RealEstate realEstate = realEstateRepository.findById(realEstateId).orElseThrow(() ->
                new IllegalStateException("realEstate with id " + realEstateId + " not exists")
        );

        if (user.getId() == realEstate.getUser().getId()) {
            return Response.builder().message("you are owner!!").build();
        }
        Save save = new Save(
                user, realEstate
        );
        saveRepository.save(save);
        return Response.builder().message("save added").build();
    }

    public Response deleteSaveRealEstate(String authHeader, int realEstateId) {

        User user = getUser(authHeader);

        RealEstate realEstate = realEstateRepository.findById(realEstateId).orElseThrow(() ->
                new IllegalStateException("realEstate with id " + realEstateId + " not exists")
        );

        Save save = new Save(
                user, realEstate
        );
        saveRepository.delete(save);

        return Response.builder().message("save deleted").build();
    }

    public Set<Save> getUserSaves(String authHeader) {
        User user = getUser(authHeader);
        return user.getSave();
    }

    public List<RealEstate> getUserUploads(String authHeader) {
        User user = getUser(authHeader);
        return user.getUploads();
    }

    public Set<Request> getUserRequests(String authHeader) {
        User user = getUser(authHeader);
        return user.getUserRequests();
    }
}
