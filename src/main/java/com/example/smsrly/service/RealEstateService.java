package com.example.smsrly.service;

import com.example.smsrly.dao.RealEstateSearchDao;
import com.example.smsrly.dao.SearchRequest;
import com.example.smsrly.dto.*;
import com.example.smsrly.entity.RealEstate;

import com.example.smsrly.entity.Request;
import com.example.smsrly.entity.User;
import com.example.smsrly.exception.InputException;
import com.example.smsrly.repository.RealEstateRepository;
import com.example.smsrly.repository.RequestRepository;
import com.example.smsrly.repository.SaveRepository;
import com.example.smsrly.utilities.PagingResponse;
import com.example.smsrly.utilities.RealEstateRequest;
import com.example.smsrly.utilities.Response;
import com.example.smsrly.utilities.Util;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


@Service
@AllArgsConstructor
public class RealEstateService {

    private final RealEstateRepository realEstateRepository;
    private final UserService userService;
    private final SaveRepository saveRepository;
    private final RequestRepository requestRepository;
    private final Util util;
    private final RealEstateDTOMapper realEstateDTOMapper;
    private final UserRequestDTOMapper userRequestDTOMapper;
    private final RealEstateSearchDao realEstateSearchDao;

    public Response deleteRealEstate(String authHeader, long realEstateId) {
        User user = userService.getUser(authHeader);
        RealEstate realEstate = realEstateRepository.findById(realEstateId).orElseThrow(() -> new InputException("realEstate with id " + realEstateId + " not exists"));
        if (!Objects.equals(user.getId(), realEstate.getUser().getId())) {
            throw new InputException(util.getMessage("real.estate.delete.access.denied"));
        }

        realEstateRepository.deleteById(realEstateId);
        return Response.builder().message(util.getMessage("real.estate.deleted")).build();
    }

    @Transactional
    public Response updateRealEstate(String authHeader, long realEstateId, RealEstateRequest request) {

        User user = userService.getUser(authHeader);
        RealEstate realEstate = realEstateRepository.findById(realEstateId).orElseThrow(() ->
                new InputException("realEstate with id " + realEstateId + " not exists")
        );

        if (!Objects.equals(user.getId(), realEstate.getUser().getId())) {
            throw new InputException(util.getMessage("real.estate.update.access.denied"));
        }

        if (isRealEstateDuplicated(request, user)) {
            throw new InputException(util.getMessage("real.estate.duplicated"));
        }

        realEstate.setTitle(request.getTitle());
        realEstate.setDescription(request.getDescription());
        realEstate.setArea(request.getArea());
        realEstate.setFloorNumber(request.getFloorNumber());
        realEstate.setBathroomNumber(request.getBathroomNumber());
        realEstate.setRoomNumber(request.getRoomNumber());
        realEstate.setPrice(request.getPrice());
        realEstate.setLatitude(request.getLatitude());
        realEstate.setLongitude(request.getLongitude());
        realEstate.setCity(request.getCity());
        realEstate.setCountry(request.getCountry());
        realEstate.setIsSale(request.getIsSale());

        return Response.builder().message(util.getMessage("real.estate.updated")).build();
    }

    private boolean isRealEstateDuplicated(RealEstateRequest request, User user) {

        return realEstateRepository.findRealEstatesWithSameDetails(
                request.getTitle(),
                request.getDescription(),
                request.getArea(),
                request.getBathroomNumber(),
                request.getRoomNumber(),
                request.getPrice(),
                request.getLatitude(),
                request.getLongitude(),
                user.getId()
        ).isPresent();
    }

    public Response uploadRealEstate(RealEstateRequest request, String authHeader) {
        User user = userService.getUser(authHeader);

        if (user.getPhoneNumber() == null) {
            throw new InputException(util.getMessage("account.phone-number.not-exists"));
        }

        if (isRealEstateDuplicated(request, user)) {
            throw new InputException(util.getMessage("real.estate.duplicated"));
        }

        int numOfUploadedRealEstatePerOneHour = realEstateRepository.findUploadedRealEstateByUserId(
                user.getId(),
                LocalDateTime.now(),
                LocalDateTime.now().minusHours(1)
        ).size();

        if (numOfUploadedRealEstatePerOneHour >= 10) {
            throw new InputException(util.getMessage("real.estate.upload.limit"));
        }


        RealEstate realEstate = RealEstate.builder()
                .title(request.getTitle())
                .bathroomNumber(request.getBathroomNumber())
                .description(request.getDescription())
                .floorNumber(request.getFloorNumber())
                .area(request.getArea())
                .price(request.getPrice())
                .longitude(request.getLongitude())
                .latitude(request.getLatitude())
                .roomNumber(request.getRoomNumber())
                .city(request.getCity())
                .country(request.getCountry())
                .isSale(request.getIsSale())
                .dateUploaded(LocalDateTime.now())
                .user(user)
                .build();

        realEstateRepository.save(realEstate);
        return Response.builder().message("real estate uploaded with id " + realEstate.getId()).build();
    }


    public PagingResponse getRealEstates(String authHeader, int page, int size) {
        User user = userService.getUser(authHeader);
        Set<Long> savedRealEstatesIds = saveRepository.findSavesByUserId(user.getId());
        realEstateDTOMapper.setSavedRealEstatesIds(savedRealEstatesIds);

        boolean isLocationExists = user.getLatitude() != null && user.getLongitude() != null;

        Pageable pageable = PageRequest.of(page, size);
        Slice<RealEstate> realEstates = isLocationExists ?
                realEstateRepository.findAllRealEstatesNearestToUser(
                        user.getLatitude(),
                        user.getLongitude(),
                        user.getId(),
                        pageable
                ) : realEstateRepository.findAllRealEstates(user.getId(), pageable);

        List<RealEstateDTO> realEstatesList = realEstates.stream()
                .map(realEstateDTOMapper)
                .toList();

        return util.pagingResponse(realEstates, realEstatesList);
    }

    public PagingResponse getRealEstateRequests(String authHeader, long realEstateId, int page, int size) {
        User user = userService.getUser(authHeader);
        RealEstate realEstate = realEstateRepository.findById(realEstateId).orElseThrow(() -> new InputException("realEstate with id " + realEstateId + " not exists"));
        if (user != realEstate.getUser()) {
            throw new InputException(util.getMessage("real.estate.not.owner"));
        }

        Pageable pageable = PageRequest.of(page, size);
        Slice<Request> requests = requestRepository.findRequestsByRealEstateId(realEstateId, pageable);
        List<UserRequestDTO> realEstateRequests = requests.stream()
                .map(userRequestDTOMapper)
                .toList();

        return util.pagingResponse(requests, realEstateRequests);
    }

    public PagingResponse getFilteredRealEstate(String authHeader, SearchRequest searchRequest, int page, int size) {
        User user = userService.getUser(authHeader);
        Set<Long> savedRealEstatesIds = saveRepository.findSavesByUserId(user.getId());
        realEstateDTOMapper.setSavedRealEstatesIds(savedRealEstatesIds);
        Pageable pageable = PageRequest.of(page, size);
        Slice<RealEstate> filters = realEstateSearchDao.findAllByCriteria(searchRequest, pageable, user);
        List<RealEstateDTO> filteredRealEstates = filters.stream().map(realEstateDTOMapper).toList();
        return util.pagingResponse(filters, filteredRealEstates);
    }
}
