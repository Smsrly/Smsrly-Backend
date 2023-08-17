package com.example.smsrly.repository;

import com.example.smsrly.entity.RealEstate;
import com.example.smsrly.entity.Request;
import com.example.smsrly.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

//    @Query(value = "SELECT * FROM request where real_estate_id = :realEstateId and user_id = :userId", nativeQuery = true)
    Optional<Request> findRequestByRealEstateAndUser(RealEstate realEstate, User user);

    @Query(value = "SELECT * FROM request WHERE real_estate_id = :realEstateId", nativeQuery = true)
    Page<Request> findRequestsByRealEstateId(long realEstateId, Pageable pageable);

    @Query(value = "SELECT * FROM request WHERE user_id = :userId and date_created < :dateTimeNow and date_created > :dateTimeBefore1hour", nativeQuery = true)
    List<Request> findRequestedRealEstateByUserId(long userId, LocalDateTime dateTimeNow, LocalDateTime dateTimeBefore1hour);

    @Query(value = "SELECT * FROM request WHERE user_id = :userId", nativeQuery = true)
    Page<Request> findRequestsByUserId(long userId, Pageable pageable);

}
