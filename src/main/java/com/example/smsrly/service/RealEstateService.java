package com.example.smsrly.service;

import com.example.smsrly.entity.RealEstate;
import com.example.smsrly.entity.Request;
import com.example.smsrly.entity.User;
import com.example.smsrly.repository.RealEstateRepository;
import com.example.smsrly.repository.RequestRepository;
import com.example.smsrly.repository.UserRepository;
import com.example.smsrly.response.OwnerInfo;
import com.example.smsrly.response.RealEstateResponse;
import com.example.smsrly.response.Response;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class RealEstateService {

    private final RealEstateRepository realEstateRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final UserService userService;

    public List<RealEstateResponse> getRealEstates(String authHeader) {

        int userId = userService.getUserId(authHeader);
        double latitude = userRepository.findLatitudeById(userId);
        double longitude = userRepository.findLongitudeById(userId);
        List<RealEstate> realEstate = realEstateRepository.getRealEstateNearestToUser(latitude, longitude, userId);
        List<RealEstateResponse> realEstateResponseList = new ArrayList<>();
        for (RealEstate estate : realEstate) {
            realEstateResponseList.add(getRealEstate(estate.getId()));
        }

        return realEstateResponseList;
    }

    public RealEstateResponse getRealEstate(int realEstateId) {
        RealEstate realEstate = realEstateRepository.findById(realEstateId).orElseThrow();
        return RealEstateResponse.builder()
                .id(realEstate.getId())
                .title(realEstate.getTitle())
                .bathroomNumber(realEstate.getBathroomNumber())
                .description(realEstate.getDescription())
                .floorNumber(realEstate.getFloorNumber())
                .image(realEstate.getImage())
                .area(realEstate.getArea())
                .price(realEstate.getPrice())
                .longitude(realEstate.getLongitude())
                .latitude(realEstate.getLatitude())
                .roomNumber(realEstate.getRoomNumber())
                .ownerInfo(OwnerInfo.builder()
                        .Name(realEstate.getUser().getFirstName() + ' ' + realEstate.getUser().getLastName())
                        .phoneNumber(realEstate.getUser().getPhoneNumber())
                        .image(realEstate.getUser().getImage()).build()
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
    public Response updateRealEstate(String authHeader, int realEstateId, String title, String description, Optional<Double> area, Optional<Integer> floorNumber, Optional<Integer> bathroomNumber, Optional<Integer> roomNumber, Optional<Double> price, Optional<Double> latitude, Optional<Double> longitude, String image) {

        User user = userService.getUser(authHeader);
        RealEstate realEstate = realEstateRepository.findById(realEstateId).orElseThrow(() ->
                new IllegalStateException("realEstate with id " + realEstateId + " not exists")
        );

        if (user.getId() != realEstate.getUser().getId()) {
            return Response.builder().message("owner only have access to delete real estate").build();
        }

        if (title != null && title.length() > 0 && !title.equals(realEstate.getTitle())) {
            realEstate.setTitle(title);
        }

        if (description != null && description.length() > 0 && !description.equals(realEstate.getDescription())) {
            realEstate.setDescription(description);
        }

        if (area.isPresent()) {
            double realEstateArea = area.get();
            if (realEstateArea > 0 && !(realEstateArea == realEstate.getArea())) {
                realEstate.setArea(realEstateArea);
            }
        }

        if (floorNumber.isPresent()) {
            int realEstateFloorNumber = floorNumber.get();
            if (realEstateFloorNumber > 0 && !(realEstateFloorNumber == realEstate.getFloorNumber())) {
                realEstate.setFloorNumber(realEstateFloorNumber);
            }
        }

        if (bathroomNumber.isPresent()) {
            int realEstateBathroomNumber = bathroomNumber.get();
            if (realEstateBathroomNumber > 0 && !(realEstateBathroomNumber == realEstate.getBathroomNumber())) {
                realEstate.setBathroomNumber(realEstateBathroomNumber);
            }
        }

        if (roomNumber.isPresent()) {
            int realEstateRoomNumber = roomNumber.get();
            if (realEstateRoomNumber > 0 && !(realEstateRoomNumber == realEstate.getRoomNumber())) {
                realEstate.setRoomNumber(realEstateRoomNumber);
            }
        }

        if (price.isPresent()) {
            double realEstatePrice = price.get();
            if (realEstatePrice > 0 && !(realEstatePrice == realEstate.getPrice())) {
                realEstate.setPrice(realEstatePrice);
            }
        }

        if (latitude.isPresent()) {
            double realEstateLatitude = latitude.get();
            if (realEstateLatitude > 0 && !(realEstateLatitude == realEstate.getLatitude())) {
                realEstate.setLatitude(realEstateLatitude);
            }
        }

        if (longitude.isPresent()) {
            double realEstateLongitude = longitude.get();
            if (realEstateLongitude > 0 && !(realEstateLongitude == realEstate.getLongitude())) {
                realEstate.setLongitude(realEstateLongitude);
            }
        }

        if (image != null && image.length() > 0 && !image.equals(realEstate.getImage())) {
            realEstate.setImage(image);
        }
        return Response.builder().message("updated").build();
    }

    public Response addRealEstate(RealEstate realEstate, String authHeader) {
        User user = userService.getUser(authHeader);
        realEstate.setUser(user);
        realEstateRepository.save(realEstate);
        return Response.builder().message("real estate added").build();
    }

    public Response addRequest(int realEstateId, String authHeader) {
        User user = userService.getUser(authHeader);
        RealEstate realEstate = realEstateRepository.findById(realEstateId).orElseThrow(() -> new IllegalStateException("realEstate with id " + realEstateId + " not exists"));

        if (user.getId() == realEstate.getUser().getId()) {
            return Response.builder().message("you are owner!!").build();
        }

        if (requestRepository.findRequest(realEstateId, user.getId()).isPresent()) {
            return Response.builder().message("Request already added").build();
        }

        Request request = new Request(
                LocalDateTime.now(),
                user,
                realEstate
        );
        requestRepository.save(request);
        return Response.builder().message("request added").build();
    }

    public List<Request> getRealEstateRequests(int realEstateId) {
        return requestRepository.getRequestsByRealEstateId(realEstateId);
    }
}
