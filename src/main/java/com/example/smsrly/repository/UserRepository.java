package com.example.smsrly.repository;

import com.example.smsrly.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    //  SELECT * FORM user WHERE email = ?
    Optional<User> findUserByEmail(String email);

    //  SELECT * FORM user WHERE phone_number = ?
    Optional<User> findUserByPhoneNumber(long phoneNumber);

    //SELECT id FROM smsrly.user where email = "youssefamr2244@gamil.com";
    @Query(value = "SELECT id FROM user where email = :email", nativeQuery = true)
    int findIdByEmail(String email);
}
