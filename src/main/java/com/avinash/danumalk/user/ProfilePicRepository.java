package com.avinash.danumalk.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfilePicRepository extends JpaRepository<ProfilePic, Long> {

    Optional<ProfilePic> findByUserUsername(String username); // Assuming you have a username field in User entity

    Optional<ProfilePic> findByName(String fileName);

    void deleteByUserUsername(String username);


}
