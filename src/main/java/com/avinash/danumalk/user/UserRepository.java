package com.avinash.danumalk.user;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> , JpaSpecificationExecutor<User> {

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.tokens WHERE u.email = :email")
    Optional<User> findByEmail(String email);

    Optional<User> findByRoles_Name(String roleName); // Add this method

    @NotNull
    Optional<User> findById(@NotNull UUID id);

    Page<User> findAllByEnabled(boolean enabled, Pageable pageable);
}
