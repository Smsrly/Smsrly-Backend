package com.example.smsrly.repository;

import com.example.smsrly.entity.RealEstate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RealEstateRepository extends JpaRepository<RealEstate, Integer> {

    @Query(value = "SELECT * FROM real_estate WHERE owner_id = :userId", nativeQuery = true)
    List<RealEstate> findUploadedRealEstateByUserId(int userId);

    @Query(value = "SELECT * FROM real_estate WHERE owner_id !=:userId ORDER BY ((latitude - :userLatitude) * (latitude - :userLatitude)) + ((longitude - :userLongitude) * (longitude - :userLongitude)) ASC", nativeQuery = true)
    List<RealEstate> getRealEstateNearestToUser(double userLatitude, double userLongitude, int userId);

}
