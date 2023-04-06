package com.example.smsrly.repository;

import com.example.smsrly.entity.RealEstateImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RealEstateImagesRepository extends JpaRepository<RealEstateImages, Integer> {

    @Query(value = "SELECT * FROM real_estate_images WHERE real_estate_id = :realEstateId", nativeQuery = true)
    List<RealEstateImages> findByRealEstateId (int realEstateId);

}
