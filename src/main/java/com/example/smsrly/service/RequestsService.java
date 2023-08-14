package com.example.smsrly.service;


import com.example.smsrly.entity.RealEstate;
import com.example.smsrly.entity.Request;
import com.example.smsrly.entity.User;
import com.example.smsrly.exception.InputException;
import com.example.smsrly.repository.RealEstateRepository;
import com.example.smsrly.repository.RequestRepository;
import com.example.smsrly.utilities.Response;
import com.example.smsrly.utilities.Util;
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
    private final Util util;

    public Response setUserRequest(long realEstateId, String authHeader) {
        User user = userService.getUser(authHeader);
        RealEstate realEstate = realEstateRepository.findById(realEstateId).orElseThrow(() -> new InputException("realEstate with id " + realEstateId + " not exists"));
        int numOfRequestsPerOneHour = requestRepository.findRequestedRealEstateByUserId(
                user.getId(),
                LocalDateTime.now(),
                LocalDateTime.now().minusHours(1)
        ).size();

        if (Objects.equals(user.getId(), realEstate.getUser().getId())) {
            throw new InputException(util.getMessage("owner.action.denied"));
        }

        if (requestRepository.findRequest(realEstateId, user.getId()).isPresent()) {
            throw new InputException(util.getMessage("real.estate.request.exist"));
        }

        if (numOfRequestsPerOneHour >= 10) {
            throw new InputException(util.getMessage("real.estate.request.time.limitation"));
        }

        Request request = new Request(
                LocalDateTime.now(),
                user,
                realEstate
        );

        requestRepository.save(request);
        return Response.builder().message(util.getMessage("real.estate.request.added")).build();
    }

    public Response deleteUserRequest(String authHeader, long realEstateId) {
        User user = userService.getUser(authHeader);

        realEstateRepository.findById(realEstateId).orElseThrow(() ->
                new InputException("realEstate with id " + realEstateId + " not exists")
        );

        Request request = requestRepository.findRequest(realEstateId, user.getId())
                .orElseThrow(() -> new InputException(util.getMessage("real.estate.request.not.exist")));

        requestRepository.delete(request);
        return Response.builder().message(util.getMessage("real.estate.request.deleted")).build();
    }

}
