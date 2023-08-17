package com.example.smsrly.repository;

import com.example.smsrly.entity.RealEstate;
import com.example.smsrly.entity.RealEstateImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RealEstateImagesRepository extends JpaRepository<RealEstateImage, Long> {

    @Query(value = "SELECT max(id) FROM real_estate_image", nativeQuery = true)
    Integer findLastIdNumber();

    //    @Query(value = "SELECT * FROM real_estate_images WHERE real_estate_id = :realEstateId", nativeQuery = true)
    List<RealEstateImage> findRealEstateImagesByRealEstate(RealEstate realEstate);

    //    @Query(value = "SELECT * FROM real_estate_images WHERE file_name = :filename", nativeQuery = true)
    Optional<RealEstateImage> findRealEstateImageByFilename(String filename);
}
