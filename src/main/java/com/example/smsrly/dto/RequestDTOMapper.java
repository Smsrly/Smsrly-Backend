package com.example.smsrly.dto;

import com.example.smsrly.entity.RealEstateImage;
import com.example.smsrly.entity.Request;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.function.Function;

@Service
public class RequestDTOMapper implements Function<Request, RealEstateDTO> {

    @Setter
    private Set<Long> savedRealEstatesIds;

    @Override
    public RealEstateDTO apply(Request request) {
        return new RealEstateDTO(
                request.getRealEstate().getId(),
                request.getRealEstate().getTitle(),
                request.getRealEstate().getDescription(),
                request.getRealEstate().getArea(),
                request.getRealEstate().getFloorNumber(),
                request.getRealEstate().getBathroomNumber(),
                request.getRealEstate().getRoomNumber(),
                request.getRealEstate().getPrice(),
                request.getRealEstate().getLatitude(),
                request.getRealEstate().getLongitude(),
                request.getRealEstate().getCity(),
                request.getRealEstate().getCountry(),
                request.getRealEstate().getIsSale(),
                request.getRealEstate().getRealEstateImages().stream()
                        .map(RealEstateImage::getImageURL)
                        .toList(),
                savedRealEstatesIds.contains(request.getRealEstate().getId()));
    }
}
