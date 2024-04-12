package com.avinash.danumalk.reactions;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReactionTypeRepository  extends JpaRepository<ReactionType, UUID> {

    // Method to find a ReactionType by its name (key)
    Optional<ReactionType> findByName(String name);
}
