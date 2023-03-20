package com.example.smsrly.service;

import com.example.smsrly.entity.RealEstate;
import com.example.smsrly.entity.Request;
import com.example.smsrly.entity.User;
import com.example.smsrly.repository.RealEstateRepository;
import com.example.smsrly.repository.RequestRepository;
import com.example.smsrly.repository.UserRepository;
import lombok.AllArgsConstructor;
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
    private final UserService userService;

    public List<RealEstate> getRealEstates(String authHeader) {

        int userId = userService.getUserId(authHeader);
        double latitude = userRepository.findLatitudeById(userId);
        double longitude = userRepository.findLongitudeById(userId);
        return realEstateRepository.getRealEstateNearestToUser(latitude, longitude, userId);
    }

    public Optional<RealEstate> getRealEstate(int realEstateId) {
        return realEstateRepository.findById(realEstateId);
    }

    public String deleteRealEstate(String authHeader, int realEstateId) {
        User user = userService.getUser(authHeader);
        RealEstate realEstate = realEstateRepository.findById(realEstateId).orElseThrow(() -> new IllegalStateException("realEstate with id " + realEstateId + " not exists"));
        if (user.getId() != realEstate.getUser().getId()) {
            return "owner only have access to delete real estate";
        }
        realEstateRepository.deleteById(realEstateId);
        return "real estate deleted";
    }

    @Transactional
    public String updateRealEstate(String authHeader, int realEstateId, String title, String description, Optional<Double> area, Optional<Integer> floorNumber, Optional<Integer> bathroomNumber, Optional<Integer> roomNumber, Optional<Double> price, Optional<Double> latitude, Optional<Double> longitude, String image) {

        User user = userService.getUser(authHeader);
        RealEstate realEstate = realEstateRepository.findById(realEstateId).orElseThrow(() ->
                new IllegalStateException("realEstate with id " + realEstateId + " not exists")
        );

        if (user.getId() != realEstate.getUser().getId()) {
            return "owner only have access to delete real estate";
        }

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

        return "updated";
    }

    public void addRealEstate(RealEstate realEstate, String authHeader) {
        User user = userService.getUser(authHeader);
        realEstate.setUser(user);
        realEstateRepository.save(realEstate);
    }

    public String addRequest(int realEstateId, String authHeader) {
        User user = userService.getUser(authHeader);
        RealEstate realEstate = realEstateRepository.findById(realEstateId).orElseThrow(() -> new IllegalStateException("realEstate with id " + realEstateId + " not exists"));

        if (user.getId() == realEstate.getUser().getId()) {
            return "you are owner!!";
        }

        if (requestRepository.findRequest(realEstateId, user.getId()).isPresent()) {
            return "Request already added";
        }

        Request request = new Request(
                LocalDateTime.now(),
                user,
                realEstate
        );
        requestRepository.save(request);
        return "request added";
    }

    public List<Request> getRealEstateRequests(int realEstateId) {
        return requestRepository.getRequestsByRealEstateId(realEstateId);
    }
}
