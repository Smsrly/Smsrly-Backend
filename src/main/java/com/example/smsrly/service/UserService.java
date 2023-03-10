package com.example.smsrly.service;

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


@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RealEstateRepository realEstateRepository;

    public void deleteUser(int userId) {

        if (!userRepository.existsById(userId)) {
            throw new IllegalStateException("user with id " + userId + " not exists");
        }
        userRepository.deleteById(userId);
    }

    @Transactional
    public void updateUser(int userId, String firstName, String lastName, String email, String password, Optional<Long> phoneNumber, Optional<Double> latitude, Optional<Double> longitude, String image) {


        User user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalStateException("user with id " + userId + " not exists")
        );

        if (firstName != null && firstName.length() > 0 && !firstName.equals(user.getFirstName())) {
            user.setFirstName(firstName);
        }

        if (lastName != null && lastName.length() > 0 && !lastName.equals(user.getLastName())) {
            user.setLastName(lastName);
        }

        if (email != null && email.length() > 0 && !email.equals(user.getEmail())) {
            Optional<User> userEmail = userRepository.findUserByEmail(email);
            if (userEmail.isPresent()) {
                throw new IllegalStateException("email is already inserted into DB");
            }

            user.setEmail(email);
        }

        if (password != null && password.length() > 0 && !passwordEncoder.matches(password, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(password));
        }

        if (phoneNumber.isPresent()) {
            long phoneNum = Long.parseLong(phoneNumber.get().toString());
            Optional<User> userPhoneNumber = userRepository.findUserByPhoneNumber(phoneNum);

            if (userPhoneNumber.isPresent()) {
                throw new IllegalStateException("phone number is already inserted into DB from another user");
            }

            if (phoneNum > 9999 && !(phoneNum == user.getPhoneNumber())) {
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

    public Optional<User> getUser(int userId) {
        return userRepository.findById(userId);
    }

    public List<RealEstate> getUserUploads(int userId) {
        return realEstateRepository.findUploadedRealEstateByUserId(userId);
    }
}
