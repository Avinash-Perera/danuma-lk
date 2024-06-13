package com.avinash.danumalk.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.tokens WHERE u.email = :email")
    Optional<User> findByEmail(String email);

    Optional<User> findByRoles_Name(String roleName); // Add this method

}
