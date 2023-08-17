package com.example.smsrly.dto;

import com.example.smsrly.entity.Request;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserRequestDTOMapper implements Function<Request, UserRequestDTO> {

    @Override
    public UserRequestDTO apply(Request request) {
        return new UserRequestDTO(
                request.getUser().getFirstname() + " " + request.getUser().getLastname(),
                request.getUser().getPhoneNumber(),
                request.getUser().getImageURL()
        );
    }
}
