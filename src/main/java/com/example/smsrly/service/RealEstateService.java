package com.example.smsrly.service;

import com.example.smsrly.entity.RealEstate;
import com.example.smsrly.entity.Request;
import com.example.smsrly.entity.User;
import com.example.smsrly.repository.RealEstateRepository;
import com.example.smsrly.repository.RequestRepository;
import com.example.smsrly.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class RealEstateService {

    private final RealEstateRepository realEstateRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;

    public List<RealEstate> getRealEstates(int userId) {
        double latitude = userRepository.findLatitudeById(userId);
        double longitude = userRepository.findLongitudeById(userId);
        return realEstateRepository.getRealEstateNearestToUser(latitude, longitude);
    }

    public Optional<RealEstate> getRealEstate(int realEstateId) {
        return realEstateRepository.findById(realEstateId);
    }

    public void deleteRealEstate(int realEstateId) {
        if (!realEstateRepository.existsById(realEstateId)) {
            throw new IllegalStateException("realEstate with id " + realEstateId + " not exists");
        }
        realEstateRepository.deleteById(realEstateId);
    }

    @Transactional
    public void updateRealEstate(int realEstateId, String title, String description, Optional<Double> area, Optional<Integer> floorNumber, Optional<Integer> bathroomNumber, Optional<Integer> roomNumber, Optional<Double> price, Optional<Double> latitude, Optional<Double> longitude, String image) {

        RealEstate realEstate = realEstateRepository.findById(realEstateId).orElseThrow(() ->
                new IllegalStateException("realEstate with id " + realEstateId + " not exists")
        );

        if (title != null && title.length() > 0 && !title.equals(realEstate.getTitle())) {
            realEstate.setTitle(title);
        }

        if (description != null && description.length() > 0 && !description.equals(realEstate.getDescription())) {
            realEstate.setDescription(description);
        }

        if (area.isPresent()) {
            double realEstateArea = Double.parseDouble(area.get().toString());
            if (realEstateArea > 0 && !(realEstateArea == realEstate.getArea())) {
                realEstate.setArea(realEstateArea);
            }
        }

        if (floorNumber.isPresent()) {
            int realEstateFloorNumber = Integer.parseInt(floorNumber.get().toString());
            if (realEstateFloorNumber > 0 && !(realEstateFloorNumber == realEstate.getFloorNumber())) {
                realEstate.setFloorNumber(realEstateFloorNumber);
            }
        }

        if (bathroomNumber.isPresent()) {
            int realEstateBathroomNumber = Integer.parseInt(bathroomNumber.get().toString());
            if (realEstateBathroomNumber > 0 && !(realEstateBathroomNumber == realEstate.getBathroomNumber())) {
                realEstate.setBathroomNumber(realEstateBathroomNumber);
            }
        }

        if (roomNumber.isPresent()) {
            int realEstateRoomNumber = Integer.parseInt(roomNumber.get().toString());
            if (realEstateRoomNumber > 0 && !(realEstateRoomNumber == realEstate.getRoomNumber())) {
                realEstate.setRoomNumber(realEstateRoomNumber);
            }
        }

        if (price.isPresent()) {
            double realEstatePrice = Double.parseDouble(price.get().toString());
            if (realEstatePrice > 0 && !(realEstatePrice == realEstate.getPrice())) {
                realEstate.setPrice(realEstatePrice);
            }
        }

        if (latitude.isPresent()) {
            double realEstateLatitude = Double.parseDouble(latitude.get().toString());
            if (realEstateLatitude > 0 && !(realEstateLatitude == realEstate.getLatitude())) {
                realEstate.setLatitude(realEstateLatitude);
            }
        }

        if (longitude.isPresent()) {
            double realEstateLongitude = Double.parseDouble(longitude.get().toString());
            if (realEstateLongitude > 0 && !(realEstateLongitude == realEstate.getLongitude())) {
                realEstate.setLongitude(realEstateLongitude);
            }
        }

        if (image != null && image.length() > 0 && !image.equals(realEstate.getImage())) {
            realEstate.setImage(image);
        }

    }

    public void addRealEstate(RealEstate realEstate) {

        realEstate.setUser(userRepository.findById(realEstate.getUserId()).orElseThrow(() -> new ResourceNotFoundException("userId not found: " + realEstate.getUserId())));
        realEstate.setUserId(realEstate.getUserId());
        realEstateRepository.save(realEstate);

    }

    public void addRequest(Request userRequest) {

        if (requestRepository.findRequest(userRequest.getRealEstateId(), userRequest.getUserId()).isPresent()) {
            throw new IllegalStateException("Request already added");
        }

        userRequest.setUser(userRepository.findById(userRequest.getUserId()).orElseThrow(() -> new ResourceNotFoundException("userId not found: " + userRequest.getUserId())));
        userRequest.setUserId(userRequest.getId());

        userRequest.setRealEstate(realEstateRepository.findById(userRequest.getRealEstateId()).orElseThrow(() -> new ResourceNotFoundException("realEstateId not found: " + userRequest.getRealEstateId())));
        userRequest.setRealEstateId(userRequest.getId());

        userRequest.setDateCreated(LocalDateTime.now());

        requestRepository.save(userRequest);
    }

    public List<Request> getRealEstateRequests(int realEstateId) {
        return requestRepository.getRequestsByRealEstateId(realEstateId);
    }
}
