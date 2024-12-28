package com.avinash.danumalk.posts;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PostTypeRepository extends JpaRepository<PostType, UUID> {
}
