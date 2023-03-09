package com.example.smsrly.service;

import com.example.smsrly.entity.RealEstate;
import com.example.smsrly.entity.User;
import com.example.smsrly.repository.RealEstateRepository;
import com.example.smsrly.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final RealEstateRepository realEstateRepository;

    public void deleteUser(int userId) {

        if (!userRepository.existsById(userId)) {
            throw new IllegalStateException("user with id " + userId + " not exists");
        }
        userRepository.deleteById(userId);
    }

    public void updateUser(int userId, String firstName, String lastName, String email, String password, long phoneNumber, double latitude, double longitude, String image) {

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
            user.setEmail(email);
        }

        if (password != null && password.length() > 0 && !password.equals(user.getPassword())) {
            user.setPassword(password);
        }
        if (phoneNumber > 9999 && !(phoneNumber == user.getPhoneNumber())) {
            user.setPhoneNumber(phoneNumber);
        }

        if (latitude > 0 && !(latitude == user.getLatitude())) {
            user.setLatitude(latitude);
        }
        if (longitude > 0 && !(longitude == user.getLongitude())) {
            user.setLongitude(longitude);
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
