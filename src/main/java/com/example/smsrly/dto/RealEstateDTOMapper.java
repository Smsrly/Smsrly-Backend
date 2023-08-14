package com.example.smsrly.dto;

import com.example.smsrly.entity.RealEstate;
import com.example.smsrly.entity.RealEstateImages;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RealEstateDTOMapper implements Function<RealEstate, RealEstateDTO> {

    @Setter
    private Set<Long> savedRealEstatesIds;

    @Override
    public RealEstateDTO apply(RealEstate realEstate) {
        return new RealEstateDTO(
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
                savedRealEstatesIds.contains(realEstate.getId())
        );
    }

}
