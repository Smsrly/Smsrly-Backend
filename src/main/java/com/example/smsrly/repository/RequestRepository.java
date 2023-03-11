package com.example.smsrly.repository;

import com.example.smsrly.entity.Request;
import com.example.smsrly.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Integer> {

    @Query(value = "SELECT * FROM request where real_estate_id = :realEstateId And user_id = :userId", nativeQuery = true)
    Optional<Request> findRequest(int realEstateId, int userId);

    @Query(value = "SELECT * FROM request where real_estate_id = :realEstateId", nativeQuery = true)
    List<Request> getRequestsByRealEstateId(int realEstateId);
}
