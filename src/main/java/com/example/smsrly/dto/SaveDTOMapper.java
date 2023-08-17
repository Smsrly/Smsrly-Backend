package com.example.smsrly.dto;

import com.example.smsrly.entity.RealEstateImage;
import com.example.smsrly.entity.Save;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class SaveDTOMapper implements Function<Save, RealEstateDTO> {
    @Override
    public RealEstateDTO apply(Save save) {

        return new RealEstateDTO(save.getRealEstate().getId(),
                save.getRealEstate().getTitle(),
                save.getRealEstate().getDescription(),
                save.getRealEstate().getArea(),
                save.getRealEstate().getFloorNumber(),
                save.getRealEstate().getBathroomNumber(),
                save.getRealEstate().getRoomNumber(),
                save.getRealEstate().getPrice(),
                save.getRealEstate().getLatitude(),
                save.getRealEstate().getLongitude(),
                save.getRealEstate().getCity(),
                save.getRealEstate().getCountry(),
                save.getRealEstate().getIsSale(),
                save.getRealEstate().getRealEstateImages().stream()
                        .map(RealEstateImage::getImageURL)
                        .toList(),
                true
        );
    }
}
