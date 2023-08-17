package com.example.smsrly.repository;

import com.example.smsrly.entity.Storage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StorageRepository extends JpaRepository<Storage, Long> {
    //    @Query(value = "SELECT * FROM storage WHERE filename = :filename", nativeQuery = true)
    Optional<Storage> findImageByFilename(String filename);

    @Query(value = "SELECT filepath FROM storage WHERE filename = :filename", nativeQuery = true)
    String findFilepathByFileName(String filename);
}
