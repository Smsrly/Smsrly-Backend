package com.example.smsrly.service;

import com.example.smsrly.entity.RealEstate;
import com.example.smsrly.entity.User;
import com.example.smsrly.repository.RealEstateRepository;
import com.example.smsrly.repository.SaveRepository;
import com.example.smsrly.repository.UserRepository;
import com.example.smsrly.response.OwnerInfo;
import com.example.smsrly.response.RealEstateResponse;
import com.example.smsrly.response.Response;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;


@Service
@AllArgsConstructor
public class RealEstateService {

    private final RealEstateRepository realEstateRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final SaveRepository saveRepository;
    private final ValidatingService validatingService;


    public List<RealEstateResponse> getRealEstates(String authHeader) {
        int userId = userService.getUserId(authHeader);
        double longitude = userRepository.findLongitudeById(userId);
        double latitude = userRepository.findLatitudeById(userId);

        List<Integer> realEstateSavesId = saveRepository.findSavesByUserId(userId);
        List<RealEstate> realEstate = realEstateRepository.getRealEstateNearestToUser(latitude, longitude, userId);
        List<RealEstateResponse> realEstateResponseList = new ArrayList<>();

        for (RealEstate estate : realEstate) {
            realEstateResponseList.add(getRealEstate(estate.getId(), realEstateSavesId.contains(estate.getId())));
        }

        return realEstateResponseList;
    }

    public RealEstateResponse getRealEstate(int realEstateId, boolean hasSaved) {
        RealEstate realEstate = realEstateRepository.findById(realEstateId).orElseThrow(() ->
                new IllegalStateException("realEstate with id " + realEstateId + " not exists"));
        return RealEstateResponse.builder()
                .id(realEstate.getId())
                .title(realEstate.getTitle())
                .bathroomNumber(realEstate.getBathroomNumber())
                .description(realEstate.getDescription())
                .floorNumber(realEstate.getFloorNumber())
                .area(realEstate.getArea())
                .price(realEstate.getPrice())
                .longitude(realEstate.getLongitude())
                .latitude(realEstate.getLatitude())
                .roomNumber(realEstate.getRoomNumber())
                .city(realEstate.getCity())
                .hasSaved(hasSaved)
                .country(realEstate.getCountry())
                .isSale(realEstate.getIsSale())
                .realEstateImages(realEstate.getRealEstateImages())
                .ownerInfo(OwnerInfo.builder()
                        .Name(realEstate.getUser().getFirstName() + ' ' + realEstate.getUser().getLastName())
                        .phoneNumber(realEstate.getUser().getPhoneNumber())
                        .image(realEstate.getUser().getImageURL())
                        .build()
                ).build();
    }

    public RealEstateResponse getRealEstate(int realEstateId) {
        RealEstate realEstate = realEstateRepository.findById(realEstateId).orElseThrow(() ->
                new IllegalStateException("realEstate with id " + realEstateId + " not exists"));
        return RealEstateResponse.builder()
                .id(realEstate.getId())
                .title(realEstate.getTitle())
                .bathroomNumber(realEstate.getBathroomNumber())
                .description(realEstate.getDescription())
                .floorNumber(realEstate.getFloorNumber())
                .area(realEstate.getArea())
                .price(realEstate.getPrice())
                .longitude(realEstate.getLongitude())
                .latitude(realEstate.getLatitude())
                .roomNumber(realEstate.getRoomNumber())
                .city(realEstate.getCity())
                .country(realEstate.getCountry())
                .isSale(realEstate.getIsSale())
                .realEstateImages(realEstate.getRealEstateImages())
                .ownerInfo(OwnerInfo.builder()
                        .Name(realEstate.getUser().getFirstName() + ' ' + realEstate.getUser().getLastName())
                        .phoneNumber(realEstate.getUser().getPhoneNumber())
                        .image(realEstate.getUser().getImageURL())
                        .build()
                ).build();
    }

    public Response deleteRealEstate(String authHeader, int realEstateId) {
        User user = userService.getUser(authHeader);
        RealEstate realEstate = realEstateRepository.findById(realEstateId).orElseThrow(() -> new IllegalStateException("realEstate with id " + realEstateId + " not exists"));
        if (user.getId() != realEstate.getUser().getId()) {
            return Response.builder().message("owner only have access to delete real estate").build();
        }

        realEstateRepository.deleteById(realEstateId);
        return Response.builder().message("real estate deleted").build();
    }

    @Transactional
    public Response updateRealEstate(String authHeader, int realEstateId, String title, String description, Optional<Double> area, Optional<Integer> floorNumber, Optional<Integer> bathroomNumber, Optional<Integer> roomNumber, Optional<Double> price, Optional<Double> latitude, Optional<Double> longitude, String city, String country, Optional<Boolean> isSale) {

        User user = userService.getUser(authHeader);
        RealEstate realEstate = realEstateRepository.findById(realEstateId).orElseThrow(() ->
                new IllegalStateException("realEstate with id " + realEstateId + " not exists")
        );

        if (user.getId() != realEstate.getUser().getId()) {
            return Response.builder().message("owner only have access to update real estate").build();
        }


        if (title != null && !title.equals(realEstate.getTitle())) {
            String validationMessage = validatingService.validatingRealEstate(title.replaceAll("\\s", ""), null, 0, 0, 0, 0, 0, null, null, 1);
            if (validationMessage != "validated") {
                return Response.builder().message(validationMessage).build();
            }
            realEstate.setTitle(title);
        }


        if (description != null && !description.equals(realEstate.getDescription())) {
            String validationMessage = validatingService.validatingRealEstate(null, description.replaceAll("\\s", ""), 0, 0, 0, 0, 0, null, null, 2);
            if (validationMessage != "validated") {
                return Response.builder().message(validationMessage).build();
            }
            realEstate.setDescription(description);
        }


        if (area.isPresent()) {
            double realEstateArea = area.get();

            if (realEstateArea != realEstate.getArea()) {
                String validationMessage = validatingService.validatingRealEstate(null, null, realEstateArea, 0, 0, 0, 0, null, null, 3);
                if (validationMessage != "validated") {
                    return Response.builder().message(validationMessage).build();
                }
                realEstate.setArea(realEstateArea);
            }
        }


        if (floorNumber.isPresent()) {
            int realEstateFloorNumber = floorNumber.get();

            if (realEstateFloorNumber != realEstate.getFloorNumber()) {

                String validationMessage = validatingService.validatingRealEstate(null, null, 0, realEstateFloorNumber, 0, 0, 0, null, null, 4);
                if (validationMessage != "validated") {
                    return Response.builder().message(validationMessage).build();
                }

                realEstate.setFloorNumber(realEstateFloorNumber);
            }
        }


        if (bathroomNumber.isPresent()) {
            int realEstateBathroomNumber = bathroomNumber.get();
            if (realEstateBathroomNumber != realEstate.getBathroomNumber()) {

                String validationMessage = validatingService.validatingRealEstate(null, null, 0, 0, realEstateBathroomNumber, 0, 0, null, null, 5);
                if (validationMessage != "validated") {
                    return Response.builder().message(validationMessage).build();
                }

                realEstate.setBathroomNumber(realEstateBathroomNumber);
            }
        }


        if (roomNumber.isPresent()) {
            int realEstateRoomNumber = roomNumber.get();
            if (realEstateRoomNumber != realEstate.getRoomNumber()) {
                String validationMessage = validatingService.validatingRealEstate(null, null, 0, 0, 0, realEstateRoomNumber, 0, null, null, 6);
                if (validationMessage != "validated") {
                    return Response.builder().message(validationMessage).build();
                }
                realEstate.setRoomNumber(realEstateRoomNumber);
            }
        }


        if (price.isPresent()) {
            double realEstatePrice = price.get();
            if (realEstatePrice != realEstate.getPrice()) {
                String validationMessage = validatingService.validatingRealEstate(null, null, 0, 0, 0, 0, realEstatePrice, null, null, 7);
                if (validationMessage != "validated") {
                    return Response.builder().message(validationMessage).build();
                }
                realEstate.setPrice(realEstatePrice);
            }
        }

        if (latitude.isPresent() && longitude.isPresent()) {
            double realEstateLatitude = latitude.get();
            double realEstateLongitude = longitude.get();

            if (realEstateLatitude != realEstate.getLatitude() || realEstateLongitude != realEstate.getLongitude()) {

                String validationMessage = validatingService.validating(null, null, null, null, 0, realEstateLatitude, realEstateLongitude, null, 6);
                if (validationMessage != "validated") {
                    return Response.builder().message(validationMessage).build();
                }

                realEstate.setLatitude(realEstateLatitude);
                realEstate.setLongitude(realEstateLongitude);
            }

            if (!city.equals(realEstate.getCity()) || !country.equals(realEstate.getCountry())) {

                String validationMessage = validatingService.validatingRealEstate(null, null, 0, 0, 0, 0, 0, city, country, 8);

                if (validationMessage != "validated") return Response.builder().message(validationMessage).build();

                realEstate.setCity(city);
                realEstate.setCountry(country);
            }
        }

        if (isSale.isPresent() && isSale.get() != realEstate.getIsSale()) realEstate.setIsSale(isSale.get());

        return Response.builder().message("updated").build();
    }

    public Response addRealEstate(RealEstate realEstate, String authHeader) {
        User user = userService.getUser(authHeader);

        Optional<RealEstate> duplicatedRealEstate = realEstateRepository.getRealEstatesWithSameDetails(realEstate.getArea(), realEstate.getBathroomNumber(), realEstate.getDescription(), realEstate.getLatitude(), realEstate.getLongitude(), realEstate.getPrice(), realEstate.getRoomNumber(), realEstate.getTitle(), userService.getUserId(authHeader));

        if (duplicatedRealEstate.isPresent()) {
            return Response.builder().message("this is duplicated real estate and that not allowed").build();
        }

        List<RealEstate> realEstateList = realEstateRepository.findUploadedRealEstateByUserId(user.getId(), LocalDateTime.now(), LocalDateTime.now().minusHours(1));

        if (realEstateList.size() != 0 && realEstateList.size() >= 10) {
            return Response.builder().message("there are upload limit, please try again after 1 hour").build();
        }

        String validationMessage = validatingService.validatingRealEstate(realEstate.getTitle().replaceAll("\\s", ""), realEstate.getDescription().replaceAll("\\s", ""), realEstate.getArea(), realEstate.getFloorNumber(), realEstate.getBathroomNumber(), realEstate.getRoomNumber(), realEstate.getPrice(), realEstate.getCity(), realEstate.getCountry(), 0);
        if (validationMessage != "validated") {
            return Response.builder().message(validationMessage).build();
        }

        String locationValidationMessage = validatingService.validating(null, null, null, null, 0, realEstate.getLatitude(), realEstate.getLongitude(), null, 6);
        if (locationValidationMessage != "validated") {
            return Response.builder().message(locationValidationMessage).build();
        }

        realEstate.setUser(user);
        realEstate.setDateUploaded(LocalDateTime.now());
        realEstateRepository.save(realEstate);
        return Response.builder().message("real estate uploaded with id " + realEstate.getId()).build();
    }


}
