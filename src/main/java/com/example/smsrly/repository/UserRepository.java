package com.example.smsrly.repository;


import com.example.smsrly.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    //  SELECT * FORM user WHERE email = ?
    Optional<User> findUserByEmail(String email);

    //SELECT id FROM user where email = "youssefamr2244@gamil.com";
    @Query(value = "SELECT id FROM user where email = :email", nativeQuery = true)
    int findIdByEmail(String email);

    @Query(value = "SELECT latitude FROM user where id = :userId", nativeQuery = true)
    double findLatitudeById(int userId);

    @Query(value = "SELECT longitude FROM user where id = :userId", nativeQuery = true)
    double findLongitudeById(int userId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE user SET enable = TRUE WHERE email = :email", nativeQuery = true)
    void enableUser(String email);
}
