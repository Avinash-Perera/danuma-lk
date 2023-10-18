package com.avinash.danumalk.reaction;

import com.avinash.danumalk.reaction.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {


    List<Reaction> findAllByPostPostId(Long postId);

    @Query("SELECT COUNT(r) FROM Like r WHERE r.post.id = :postId")
    int countLikesByPostId(@Param("postId") Long postId);

    @Query("SELECT COUNT(r) FROM Dislike r WHERE r.post.id = :postId")
    int countDislikesByPostId(@Param("postId") Long postId);


}
