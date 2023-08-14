package com.example.smsrly.dto;

import com.example.smsrly.entity.RealEstate;
import com.example.smsrly.entity.RealEstateImages;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserRealEstateDTOMapper implements Function<RealEstate, UserRealEstateDTO> {

    private final UserRequestDTOMapper userRequestDTOMapper;

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
                realEstate.getRealEstateImages().stream()
                        .map(RealEstateImages::getRealEstateImageURL)
                        .collect(Collectors.toList()),
                realEstate.getRealEstateRequest().stream()
                        .map(userRequestDTOMapper)
                        .collect(Collectors.toSet())
        );
    }
}
