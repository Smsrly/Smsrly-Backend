package com.example.smsrly.repository;

import com.example.smsrly.entity.RealEstateImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RealEstateImagesRepository extends JpaRepository<RealEstateImages, Long> {

    @Query(value = "SELECT max(id) FROM real_estate_images", nativeQuery = true)
    Integer getLastIdNumber();

    @Query(value = "SELECT * FROM real_estate_images WHERE real_estate_id = :realEstateId", nativeQuery = true)
    List<RealEstateImages> findByRealEstateId(long realEstateId);

    @Query(value = "SELECT * FROM real_estate_images WHERE name = :fileName", nativeQuery = true)
    Optional<RealEstateImages> findByImageURL(String fileName);
}
