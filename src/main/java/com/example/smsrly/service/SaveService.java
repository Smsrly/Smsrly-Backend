package com.example.smsrly.service;

import com.example.smsrly.entity.RealEstate;
import com.example.smsrly.entity.Save;
import com.example.smsrly.entity.User;
import com.example.smsrly.exception.InputException;
import com.example.smsrly.repository.RealEstateRepository;
import com.example.smsrly.repository.SaveRepository;
import com.example.smsrly.utilities.Response;
import com.example.smsrly.utilities.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SaveService {

    private final UserService userService;
    private final RealEstateRepository realEstateRepository;
    private final SaveRepository saveRepository;
    private final Util util;

    public Response saveRealEstate(String authHeader, long realEstateId) {

        User user = userService.getUser(authHeader);

        RealEstate realEstate = realEstateRepository.findById(realEstateId).orElseThrow(() ->
                new InputException("realEstate with id " + realEstateId + " not exists")
        );

        if (Objects.equals(user.getId(), realEstate.getUser().getId())) {
            throw new InputException(util.getMessage("owner.action.denied"));
        }


        if (saveRepository.findSaveByRealEstateAndUser(realEstate, user).isPresent()) {
            throw new InputException(util.getMessage("real.estate.save.exist"));
        }

        Save save = new Save(
                user, realEstate
        );
        saveRepository.save(save);
        return Response.builder().message(util.getMessage("real.estate.save.added")).build();
    }

    public Response deleteSaveRealEstate(String authHeader, long realEstateId) {

        User user = userService.getUser(authHeader);

        RealEstate realEstate = realEstateRepository.findById(realEstateId).orElseThrow(() ->
                new InputException("realEstate with id " + realEstateId + " not exists")
        );

        Save save = saveRepository.findSaveByRealEstateAndUser(realEstate, user).orElseThrow(() ->
                new InputException(util.getMessage("real.estate.save.not.exist"))
        );

        saveRepository.delete(save);

        return Response.builder().message(util.getMessage("real.estate.save.deleted")).build();
    }

}
