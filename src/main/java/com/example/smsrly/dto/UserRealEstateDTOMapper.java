package com.example.smsrly.dto;

import com.example.smsrly.entity.RealEstate;
import com.example.smsrly.entity.RealEstateImage;
import com.example.smsrly.repository.RequestRepository;
import lombok.Setter;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserRealEstateDTOMapper implements Function<RealEstate, UserRealEstateDTO> {

    @Setter
    private Pageable pageable;
    private final UserRequestDTOMapper userRequestDTOMapper;
    private final RequestRepository requestRepository;

    @Override
    public UserRealEstateDTO apply(RealEstate realEstate) {
        return new UserRealEstateDTO(
                realEstate.getId(),
                realEstate.getTitle(),
                realEstate.getDescription(),
                realEstate.getArea(),
                realEstate.getFloorNumber(),
                realEstate.getBathroomNumber(),
                realEstate.getRoomNumber(),
                realEstate.getPrice(),
                realEstate.getLatitude(),
                realEstate.getLongitude(),
                realEstate.getCity(),
                realEstate.getCountry(),
                realEstate.getIsSale(),
                realEstate.getRealEstateImages()
                        .stream()
                        .map(RealEstateImage::getImageURL)
                        .toList(),
                requestRepository.findRequestsByRealEstateId(realEstate.getId(), pageable)
                        .stream()
                        .map(userRequestDTOMapper)
                        .toList()
        );
    }

    public UserRealEstateDTOMapper(UserRequestDTOMapper userRequestDTOMapper, RequestRepository requestRepository) {
        this.userRequestDTOMapper = userRequestDTOMapper;
        this.requestRepository = requestRepository;
    }
}
