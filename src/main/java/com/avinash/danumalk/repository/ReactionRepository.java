package com.avinash.danumalk.repository;

import com.avinash.danumalk.model.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    List<Reaction> findAllByPostPostId(Long postId);
}
