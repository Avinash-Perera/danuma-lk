package com.avinash.danumalk.reactions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeReactionRepository extends JpaRepository<LikeReaction, Long> {
    // Define custom methods if needed
}
