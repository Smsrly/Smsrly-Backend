package com.example.smsrly.service;

import com.example.smsrly.entity.RealEstate;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SaveService {

    private final UserService userService;
    private final RealEstateRepository realEstateRepository;
    private final SaveRepository saveRepository;
    private final RequestRepository requestRepository;

    public Response saveRealEstate(String authHeader, int realEstateId) {

        User user = userService.getUser(authHeader);

        RealEstate realEstate = realEstateRepository.findById(realEstateId).orElseThrow(() ->
                new IllegalStateException("realEstate with id " + realEstateId + " not exists")
        );

        if (user.getId() == realEstate.getUser().getId()) {
            return Response.builder().message("you are owner!!").build();
        }


        if (saveRepository.findSave(realEstateId, user.getId()).isPresent()) {
            return Response.builder().message("save already added").build();
        }

        Save save = new Save(
                user, realEstate
        );
        saveRepository.save(save);
        return Response.builder().message("save added").build();
    }

    public Response deleteSaveRealEstate(String authHeader, int realEstateId) {

        User user = userService.getUser(authHeader);

        realEstateRepository.findById(realEstateId).orElseThrow(() ->
                new IllegalStateException("realEstate with id " + realEstateId + " not exists")
        );

        Optional<Save> save = saveRepository.findSave(realEstateId, user.getId());

        if (save.isEmpty()) {
            return Response.builder().message("the real estate is not already on the saved list").build();
        }

        saveRepository.delete(save.get());

        return Response.builder().message("save deleted").build();
    }

    public List<RealEstateResponse> getUserSaves(String authHeader) {
        User user = userService.getUser(authHeader);
        Set<Save> saves = user.getSave();
        Set<Integer> requests = requestRepository.findRequestByUserId(user.getId());
        List<RealEstateResponse> realEstateResponseList = new ArrayList<>();
        for (var item: saves){
            realEstateResponseList.add(
                    getRealEstate(
                            item.getRealEstate(),
                            true,
                            requests.contains(item.getRealEstate().getId())
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
