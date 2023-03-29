package com.example.smsrly.repository;

import com.example.smsrly.entity.RealEstate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RealEstateRepository extends JpaRepository<RealEstate, Integer> {

    @Query(value = "SELECT * FROM real_estate WHERE owner_id = :userId and date_uploaded < :dateTimeNow and date_uploaded > :dateTimeBefore1hour", nativeQuery = true)
    List<RealEstate> findUploadedRealEstateByUserId(int userId, LocalDateTime dateTimeNow, LocalDateTime dateTimeBefore1hour);

    @Query(value = "SELECT * FROM real_estate WHERE owner_id !=:userId ORDER BY ((latitude - :userLatitude) * (latitude - :userLatitude)) + ((longitude - :userLongitude) * (longitude - :userLongitude)) ASC", nativeQuery = true)
    List<RealEstate> getRealEstateNearestToUser(double userLatitude, double userLongitude, int userId);

}
