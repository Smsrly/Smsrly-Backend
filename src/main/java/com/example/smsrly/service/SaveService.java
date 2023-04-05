package com.example.smsrly.service;

import com.example.smsrly.entity.RealEstate;
import com.example.smsrly.entity.Save;
import com.example.smsrly.entity.User;
import com.example.smsrly.repository.RealEstateRepository;
import com.example.smsrly.repository.SaveRepository;
import com.example.smsrly.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SaveService {

    private final UserService userService;
    private final RealEstateRepository realEstateRepository;
    private final SaveRepository saveRepository;

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

    public Set<Save> getUserSaves(String authHeader) {
        User user = userService.getUser(authHeader);
        return user.getSave();
    }
}
