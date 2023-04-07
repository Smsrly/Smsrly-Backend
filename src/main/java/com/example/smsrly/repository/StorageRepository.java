package com.example.smsrly.repository;

import com.example.smsrly.entity.Storage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StorageRepository extends JpaRepository<Storage, Integer> {
    @Query(value = "SELECT * FROM storage WHERE name = :fileName", nativeQuery = true)
    Optional<Storage> findImageByName(String fileName);

    @Query(value = "SELECT path FROM storage WHERE name = :fileName", nativeQuery = true)
    String findImagePathByImageName(String fileName);
}
