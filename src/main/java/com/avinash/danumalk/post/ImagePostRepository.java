package com.avinash.danumalk.post;

import com.avinash.danumalk.post.ImagePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ImagePostRepository extends JpaRepository<ImagePost, Long> {

//    @Modifying
//    @Transactional
//    @Query("UPDATE ImagePost ip SET ip.imageUrls = :imageUrls WHERE ip.postId = :postId")
//    void updateImageUrls(@Param("postId") Long postId, @Param("imageUrls") List<String> imageUrls);
}
