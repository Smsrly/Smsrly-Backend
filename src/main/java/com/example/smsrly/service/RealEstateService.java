package com.example.smsrly.service;

import com.example.smsrly.entity.RealEstate;
import com.example.smsrly.repository.RealEstateRepository;
import com.example.smsrly.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class RealEstateService {

    private final RealEstateRepository repository;
    private final UserRepository userRepository;

    // need some updates
    public List<RealEstate> getRealEstates() {
        return repository.findAll();
    }

    public Optional<RealEstate> getRealEstate(int realEstateId) {
        return repository.findById(realEstateId);
    }

    public void deleteRealEstate(int realEstateId) {
        if (!repository.existsById(realEstateId)) {
            throw new IllegalStateException("realEstate with id " + realEstateId + " not exists");
        }
        repository.deleteById(realEstateId);
    }

    public void updateRealEstate(int realEstateId, String title, String description, double area, int floorNumber, int bathroomNumber, int roomNumber, double price, double latitude, double longitude, String image) {

        RealEstate realEstate = repository.findById(realEstateId).orElseThrow(() ->
                new IllegalStateException("realEstate with id " + realEstateId + " not exists")
        );

        if (title != null && title.length() > 0 && !title.equals(realEstate.getTitle())) {
            realEstate.setTitle(title);
        }

        if (description != null && description.length() > 0 && !description.equals(realEstate.getDescription())) {
            realEstate.setDescription(description);
        }


        if (area > 0 && !(area == realEstate.getArea())) {
            realEstate.setArea(area);
        }

        if (floorNumber > 0 && !(floorNumber == realEstate.getFloorNumber())) {
            realEstate.setFloorNumber(floorNumber);
        }
        if (bathroomNumber > 0 && !(bathroomNumber == realEstate.getBathroomNumber())) {
            realEstate.setBathroomNumber(bathroomNumber);
        }

        if (roomNumber > 0 && !(roomNumber == realEstate.getRoomNumber())) {
            realEstate.setRoomNumber(roomNumber);
        }

        if (price > 0 && !(price == realEstate.getPrice())) {
            realEstate.setPrice(price);
        }

        if (latitude > 0 && !(latitude == realEstate.getLatitude())) {
            realEstate.setLatitude(latitude);
        }


        if (longitude > 0 && !(longitude == realEstate.getLongitude())) {
            realEstate.setLongitude(longitude);
        }

        if (image != null && image.length() > 0 && !image.equals(realEstate.getImage())) {
            realEstate.setImage(image);
        }

    }

    public void addRealEstate(RealEstate realEstate) {

        realEstate.setUser(userRepository.findById(realEstate.getUserId()).orElseThrow(() -> new ResourceNotFoundException("userId not found: " + realEstate.getUserId())));
        realEstate.setUserId(realEstate.getUserId());
        repository.save(realEstate);

    }
}
