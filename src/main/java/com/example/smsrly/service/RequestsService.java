package com.example.smsrly.service;


import com.example.smsrly.entity.RealEstate;
import com.example.smsrly.entity.Request;
import com.example.smsrly.entity.Save;
import com.example.smsrly.entity.User;
import com.example.smsrly.repository.RealEstateRepository;
import com.example.smsrly.repository.RequestRepository;
import com.example.smsrly.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RequestsService {

    private final UserService userService;
    private final RealEstateRepository realEstateRepository;
    private final RequestRepository requestRepository;

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

    public Set<Request> getUserRequests(String authHeader) {
        User user = userService.getUser(authHeader);
        return user.getUserRequests();
    }

}
