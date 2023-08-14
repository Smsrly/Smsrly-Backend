package com.example.smsrly.service;

import com.example.smsrly.auth.RegistrationRequest;
import com.example.smsrly.dto.*;
import com.example.smsrly.entity.User;
import com.example.smsrly.exception.InputException;
import com.example.smsrly.repository.SaveRepository;
import com.example.smsrly.repository.UserRepository;
import com.example.smsrly.utilities.Response;
import com.example.smsrly.utilities.Util;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SaveRepository saveRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserDTOMapper userDTOMapper;
    private final UserRealEstateDTOMapper userRealEstateDTOMapper;
    private final RequestDTOMapper requestDTOMapper;
    private final SaveDTOMapper saveDTOMapper;
    private final Util util;

    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(() -> new InputException(util.getMessage("account.not.exists")));
    }

    public Boolean isUserExists(String email) {
        return userRepository.findUserByEmail(email).isPresent();
    }

    public String extractUserEmail(String authHeader) {
        String token = jwtService.extractToken(authHeader);
        return jwtService.extractEmail(token);
    }

    public UserDTO getUserInfo(String authHeader) {
        String email = extractUserEmail(authHeader);

        return userRepository.findUserByEmail(email)
                .map(userDTOMapper)
                .orElseThrow(() -> new InputException(util.getMessage("token.user.not.exists")));
    }

    public User getUser(String authHeader) {
        String userEmail = extractUserEmail(authHeader);
        return userRepository.findUserByEmail(userEmail).orElseThrow(() -> new InputException(util.getMessage("token.user.not.exists")));
    }

    public Response deleteUser(String authHeader) {
        User user = getUser(authHeader);
        userRepository.deleteById(user.getId());
        return Response.builder().message(util.getMessage("account.deleted")).build();
    }

    @Transactional
    public Response updateUser(String authHeader, RegistrationRequest request) {

        User user = getUser(authHeader);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InputException(util.getMessage("account.password.not.matched"));
        }

        if (!Objects.equals(request.getEmail(), user.getEmail())) {
            throw new InputException(util.getMessage("account.changing.email.error"));
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setLongitude(request.getLongitude());
        user.setLatitude(request.getLatitude());
        return Response.builder().message(util.getMessage("account.updated")).build();
    }


    public List<UserRealEstateDTO> getUserUploads(String authHeader) {
        User user = getUser(authHeader);
        return user.getUploads().stream()
                .map(userRealEstateDTOMapper)
                .collect(Collectors.toList());
    }


    public List<RealEstateDTO> getUserRequests(String authHeader) {
        User user = getUser(authHeader);
        Set<Long> savedRealEstatesIds = saveRepository.findSavesByUserId(user.getId());
        requestDTOMapper.setSavedRealEstatesIds(savedRealEstatesIds);

        return user.getUserRequests().stream()
                .map(requestDTOMapper)
                .collect(Collectors.toList());
    }

    public List<RealEstateDTO> getUserSaves(String authHeader) {
        User user = getUser(authHeader);
        return user.getSave().stream()
                .map(saveDTOMapper)
                .collect(Collectors.toList());
    }

}
