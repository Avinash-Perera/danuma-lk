package com.avinash.danumalk.Reactions.Reaction;

import com.avinash.danumalk.Reactions.ReactionType.ReactionType;
import com.avinash.danumalk.posts.BasePostEntity;
import com.avinash.danumalk.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReactionRepository extends JpaRepository<Reaction, UUID> {
    Optional<Reaction> findByPostAndOwnerAndReactionType(BasePostEntity post, User owner, ReactionType reactionType);
    void deleteByPostAndOwnerAndReactionType(BasePostEntity post, User owner, ReactionType reactionType);
}