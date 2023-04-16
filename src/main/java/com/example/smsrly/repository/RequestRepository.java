package com.example.smsrly.repository;

import com.example.smsrly.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {

    @Query(value = "SELECT * FROM request where real_estate_id = :realEstateId and user_id = :userId", nativeQuery = true)
    Optional<Request> findRequest(int realEstateId, int userId);

    @Query(value = "SELECT * FROM request where real_estate_id = :realEstateId", nativeQuery = true)
    List<Request> getRequestsByRealEstateId(int realEstateId);

    @Query(value = "SELECT * FROM request WHERE user_id = :userId and date_created < :dateTimeNow and date_created > :dateTimeBefore1hour", nativeQuery = true)
    List<Request> findRequestedRealEstateByUserId(int userId, LocalDateTime dateTimeNow, LocalDateTime dateTimeBefore1hour);

    @Query(value = "SELECT real_estate_id FROM request where user_id = :userId", nativeQuery = true)
    Set<Integer> findRequestByUserId(int userId);
}
