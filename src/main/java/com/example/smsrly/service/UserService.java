package com.example.smsrly.service;

import com.example.smsrly.auth.RegistrationRequest;
import com.example.smsrly.dto.*;
import com.example.smsrly.entity.RealEstate;
import com.example.smsrly.entity.Request;
import com.example.smsrly.entity.Save;
import com.example.smsrly.entity.User;
import com.example.smsrly.exception.InputException;
import com.example.smsrly.repository.RealEstateRepository;
import com.example.smsrly.repository.RequestRepository;
import com.example.smsrly.repository.SaveRepository;
import com.example.smsrly.repository.UserRepository;
import com.example.smsrly.utilities.PagingResponse;
import com.example.smsrly.utilities.Response;
import com.example.smsrly.utilities.Util;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;


@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SaveRepository saveRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserDTOMapper userDTOMapper;
    private final UserRealEstateDTOMapper userRealEstateDTOMapper;
    private final RealEstateRepository realEstateRepository;
    private final RequestRepository requestRepository;
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

    public UserDTO getUserInfo(String authHeader, int page, int size) {
        String email = extractUserEmail(authHeader);
        Pageable pageable = PageRequest.of(page, size);
        userDTOMapper.setPageable(pageable);
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

        if (!Objects.equals(request.getEmail(), user.getEmail())) {
            throw new InputException(util.getMessage("account.changing.email.error"));
        }

        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setLongitude(request.getLongitude());
        user.setLatitude(request.getLatitude());
        return Response.builder().message(util.getMessage("account.updated")).build();
    }


    public PagingResponse getUserUploads(String authHeader, int page, int size) {
        User user = getUser(authHeader);
        Pageable pageable = PageRequest.of(page, size);
        Slice<RealEstate> uploads = realEstateRepository.findUserUploads(user.getId(), pageable);
        List<UserRealEstateDTO> userUploads = uploads.stream()
                .map(userRealEstateDTOMapper)
                .toList();
        return util.pagingResponse(uploads, userUploads);
    }


    public PagingResponse getUserRequests(String authHeader, int page, int size) {
        User user = getUser(authHeader);
        Set<Long> savedRealEstatesIds = saveRepository.findSavesByUserId(user.getId());
        requestDTOMapper.setSavedRealEstatesIds(savedRealEstatesIds);
        Pageable pageable = PageRequest.of(page, size);
        Slice<Request> requests = requestRepository.findRequestsByUserId(user.getId(), pageable);
        List<RealEstateDTO> userRequests = requests.stream()
                .map(requestDTOMapper)
                .toList();
        return util.pagingResponse(requests, userRequests);
    }

    public PagingResponse getUserSaves(String authHeader, int page, int size) {
        User user = getUser(authHeader);
        Pageable pageable = PageRequest.of(page, size);
        Slice<Save> saves = saveRepository.findSavesByUserId(user.getId(), pageable);
        List<RealEstateDTO> userSaves = saves.stream()
                .map(saveDTOMapper)
                .toList();
        return util.pagingResponse(saves, userSaves);
    }

}
