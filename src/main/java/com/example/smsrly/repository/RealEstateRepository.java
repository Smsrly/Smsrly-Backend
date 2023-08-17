package com.example.smsrly.repository;

import com.example.smsrly.entity.RealEstate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query(value = "SELECT * FROM real_estate WHERE owner_id != :userId ORDER BY ((latitude - :userLatitude) * (latitude - :userLatitude)) + ((longitude - :userLongitude) * (longitude - :userLongitude)) ASC",
            countQuery = "SELECT count(*) FROM real_estate WHERE owner_id != :userId", // Count query is needed for paging
            nativeQuery = true)
    Page<RealEstate> findAllRealEstatesNearestToUser(double userLatitude, double userLongitude, long userId, Pageable pageable);

    @Query(value = "SELECT * FROM real_estate WHERE owner_id !=:userId", nativeQuery = true)
    Page<RealEstate> findAllRealEstates(long userId, Pageable pageable);

    @Query(value = "SELECT * FROM real_estate where area = :area and bathroom_number = :bathroomNumber and description = :description and latitude = :latitude and longitude = :longitude and price = :price and room_number = :roomNumber and title = :title and owner_id = :ownerId", nativeQuery = true)
    Optional<RealEstate> findRealEstatesWithSameDetails(String title, String description, double area, int bathroomNumber, int roomNumber, double price, double latitude, double longitude, long ownerId);

    @Query(value = "SELECT * FROM real_estate WHERE owner_id = :userId", nativeQuery = true)
    Page<RealEstate> findUserUploads(long userId, Pageable pageable);

}
