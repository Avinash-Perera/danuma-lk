package com.avinash.danumalk.posts.image_post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ImagePostRepository extends JpaRepository<ImagePost, UUID>, JpaSpecificationExecutor<ImagePost>,ImagePostRepositoryCustom  {
    @Query("""
            SELECT ipost
            FROM ImagePost ipost
            ORDER BY ipost.createdDate desc
           """)
    Page<ImagePost> findAllPosts(Pageable pageable);
}



