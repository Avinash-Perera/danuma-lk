package com.avinash.danumalk.posts;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostCategoryRepository extends JpaRepository<PostCategory, UUID> {
}
