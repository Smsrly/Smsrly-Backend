package com.example.smsrly.repository;

import com.example.smsrly.entity.Save;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface SaveRepository extends JpaRepository<Save, Integer> {
    @Query(value = "SELECT * FROM save where real_estate_id = :realEstateId and user_id = :userId", nativeQuery = true)
    Optional<Save> findSave(int realEstateId, int userId);

    @Query(value = "SELECT real_estate_id FROM save where user_id = :userId", nativeQuery = true)
    Set<Integer> findSavesByUserId(int userId);
}
