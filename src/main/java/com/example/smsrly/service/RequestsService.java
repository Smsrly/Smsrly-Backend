package com.example.smsrly.service;


import com.example.smsrly.entity.RealEstate;
import com.example.smsrly.entity.Request;
import com.example.smsrly.entity.Save;
import com.example.smsrly.entity.User;
import com.example.smsrly.repository.RealEstateRepository;
import com.example.smsrly.repository.RequestRepository;
import com.example.smsrly.repository.SaveRepository;
import com.example.smsrly.response.RealEstateResponse;
import com.example.smsrly.response.Response;
import com.example.smsrly.response.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RequestsService {

    private final UserService userService;
    private final RealEstateRepository realEstateRepository;
    private final RequestRepository requestRepository;
    private final SaveRepository saveRepository;

    public Response addRequest(int realEstateId, String authHeader) {
        User user = userService.getUser(authHeader);
        RealEstate realEstate = realEstateRepository.findById(realEstateId).orElseThrow(() -> new IllegalStateException("realEstate with id " + realEstateId + " not exists"));
        List<Request> requests = requestRepository.findRequestedRealEstateByUserId(user.getId(), LocalDateTime.now(), LocalDateTime.now().minusHours(1));

        if (user.getId() == realEstate.getUser().getId()) {
            return Response.builder().message("you are owner!!").build();
        }

        if (requestRepository.findRequest(realEstateId, user.getId()).isPresent()) {
            return Response.builder().message("request already added").build();
        }

        if (requests.size() != 0 && requests.size() >= 10) {
            return Response.builder().message("there are request limit, please try again after 1 hour").build();
        }

        Request request = new Request(
                LocalDateTime.now(),
                user,
                realEstate
        );

        requestRepository.save(request);
        return Response.builder().message("request added").build();
    }

    public Response deleteUserRequest(String authHeader, int realEstateId) {
        User user = userService.getUser(authHeader);

        realEstateRepository.findById(realEstateId).orElseThrow(() ->
                new IllegalStateException("realEstate with id " + realEstateId + " not exists")
        );

        Optional<Request> request = requestRepository.findRequest(realEstateId, user.getId());
        if (request.isEmpty()) {
            return Response.builder().message("the real estate is not already on the request list").build();
        }

        requestRepository.delete(request.get());

        return Response.builder().message("request deleted").build();

    }

    public List<Request> getRealEstateRequests(int realEstateId) {
        return requestRepository.getRequestsByRealEstateId(realEstateId);
    }

    public List<RealEstateResponse> getUserRequests(String authHeader) {
        User user = userService.getUser(authHeader);
        Set<Request> requests = user.getUserRequests();
        Set<Integer> saves = saveRepository.findSavesByUserId(user.getId());
        List<RealEstateResponse> realEstateResponseList = new ArrayList<>();
        for (var req : requests){
            realEstateResponseList.add(
                    getRealEstate(
                            req.getRealEstate(),
                            saves.contains(req.getRealEstate().getId()),
                            true
                    )
            );
        }
        return realEstateResponseList;
    }
    public RealEstateResponse getRealEstate(RealEstate realEstate, boolean hasSaved,boolean hasRequested) {
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
                .hasRequested(hasRequested)
                .hasSaved(hasSaved)
                .country(realEstate.getCountry())
                .isSale(realEstate.getIsSale())
                .realEstateImages(realEstate.getRealEstateImages())
                .userInfo(UserInfo.builder()
                        .Name(realEstate.getUser().getFirstName() + ' ' + realEstate.getUser().getLastName())
                        .phoneNumber(realEstate.getUser().getPhoneNumber())
                        .image(realEstate.getUser().getImageURL())
                        .build()
                ).build();
    }

}
