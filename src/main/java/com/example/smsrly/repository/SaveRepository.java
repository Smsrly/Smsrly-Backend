package com.example.smsrly.repository;

import com.example.smsrly.entity.RealEstate;
import com.example.smsrly.entity.Save;
import com.example.smsrly.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface SaveRepository extends JpaRepository<Save, Long> {
    //    @Query(value = "SELECT * FROM save where real_estate_id = :realEstateId and user_id = :userId", nativeQuery = true)
    Optional<Save> findSaveByRealEstateAndUser(RealEstate realEstate, User user);

    @Query(value = "SELECT real_estate_id FROM save where user_id = :userId", nativeQuery = true)
    Set<Long> findSavesByUserId(long userId);

    @Query(value = "SELECT * FROM save where user_id = :userId", nativeQuery = true)
    Page<Save> findSavesByUserId(long userId, Pageable pageable);
}
