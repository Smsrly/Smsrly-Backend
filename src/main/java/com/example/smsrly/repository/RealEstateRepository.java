package com.example.smsrly.repository;

import com.example.smsrly.entity.RealEstate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RealEstateRepository extends JpaRepository<RealEstate, Long> {

    @Query(value = "SELECT * FROM real_estate WHERE owner_id = :userId and date_uploaded < :dateTimeNow and date_uploaded > :dateTimeBefore1hour", nativeQuery = true)
    List<RealEstate> findUploadedRealEstateByUserId(long userId, LocalDateTime dateTimeNow, LocalDateTime dateTimeBefore1hour);

    @Query(value = "SELECT * FROM real_estate WHERE owner_id !=:userId ORDER BY ((latitude - :userLatitude) * (latitude - :userLatitude)) + ((longitude - :userLongitude) * (longitude - :userLongitude)) ASC", nativeQuery = true)
    List<RealEstate> getRealEstateNearestToUser(double userLatitude, double userLongitude, long userId);


    @Query(value = "SELECT * FROM real_estate WHERE owner_id !=:userId", nativeQuery = true)
    List<RealEstate> getAllRealEstates(long userId);


    @Query(value = "SELECT * FROM real_estate where area = :area and bathroom_number = :bathroomNumber and description = :description and latitude = :latitude and longitude = :longitude and price = :price and room_number = :roomNumber and title = :title and owner_id = :ownerId", nativeQuery = true)
    Optional<RealEstate> getRealEstatesWithSameDetails(double area, int bathroomNumber, String description, double latitude, double longitude, double price, int roomNumber, String title, long ownerId);

}
