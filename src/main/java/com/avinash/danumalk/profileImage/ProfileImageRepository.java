package com.avinash.danumalk.profileImage;

import com.avinash.danumalk.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileImageRepository extends JpaRepository<ProfileImage,Long> {


    Optional<ProfileImage> findByName(String fileName);

    Optional<ProfileImage> findByUser(User user);

}
