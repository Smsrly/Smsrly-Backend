package com.example.smsrly.repository;

import com.example.smsrly.entity.RealEstate;
import com.example.smsrly.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RealEstateRepository extends JpaRepository<RealEstate, Integer> {

    @Query(value = "SELECT * FROM real_estate WHERE owner_id = :userId", nativeQuery = true)
    List<RealEstate> findUploadedRealEstateByUserId(int userId);

}
