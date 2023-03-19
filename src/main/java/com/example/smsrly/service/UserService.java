package com.example.smsrly.service;

import com.example.smsrly.config.JwtService;
import com.example.smsrly.entity.RealEstate;
import com.example.smsrly.entity.User;
import com.example.smsrly.repository.RealEstateRepository;
import com.example.smsrly.repository.UserRepository;
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

//    public Optional<User> getUser(String authHeader) {
//        return userRepository.findUserByEmail(extractEmailFromToken(authHeader));
//    }

    public void deleteUser(String authHeader) {
        userRepository.deleteById(getUserId(authHeader));
    }

    @Transactional
    public void updateUser(String authHeader, String firstName, String lastName, String password, Optional<Long> phoneNumber, Optional<Double> latitude, Optional<Double> longitude, String image) {

        User user = getUser(authHeader);

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
            long phoneNum = Long.parseLong(phoneNumber.get().toString());

            if (phoneNum > 9999 && phoneNum != user.getPhoneNumber()) {
                user.setPhoneNumber(phoneNum);
            }
        }

        if (latitude.isPresent()) {
            double lat = Double.parseDouble(latitude.get().toString());
            if (lat > 0 && !(lat == user.getLatitude())) {
                user.setLongitude(lat);
            }
        }

        if (longitude.isPresent()) {
            double lon = Double.parseDouble(longitude.get().toString());
            if (lon > 0 && !(lon == user.getLongitude())) {
                user.setLatitude(lon);
            }
        }

        if (image != null && image.length() > 0 && !image.equals(user.getImage())) {
            user.setImage(image);
        }

    }

    public void saveRealEstate(String authHeader, int realEstateId) {

        User user = getUser(authHeader);

        RealEstate realEstate = realEstateRepository.findById(realEstateId).orElseThrow(() ->
                new IllegalStateException("realEstate with id " + realEstateId + " not exists")
        );

        Set<RealEstate> realEstateList = user.getSave();
        realEstateList.add(realEstate);
        user.setSave(realEstateList);
        userRepository.save(user);
    }

    public void deleteSaveRealEstate(String authHeader, int realEstateId) {

        User user = getUser(authHeader);

        RealEstate realEstate = realEstateRepository.findById(realEstateId).orElseThrow(() ->
                new IllegalStateException("realEstate with id " + realEstateId + " not exists")
        );

        Set<RealEstate> realEstateList = user.getSave();
        realEstateList.remove(realEstate);
        user.setSave(realEstateList);
        userRepository.save(user);
    }

    public Set<RealEstate> getUserSaves(String authHeader) {
        User user = getUser(authHeader);
        return user.getSave();
    }

    public List<RealEstate> getUserUploads(String authHeader) {
        User user = getUser(authHeader);
        return realEstateRepository.findUploadedRealEstateByUserId(user.getId());
    }
}
