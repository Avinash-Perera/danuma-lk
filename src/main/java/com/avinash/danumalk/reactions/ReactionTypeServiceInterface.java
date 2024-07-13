package com.avinash.danumalk.reactions;

import java.util.List;
import java.util.UUID;

public interface ReactionTypeServiceInterface {

    List<ReactionType> getAllReactionTypes();

    // Get a ReactionType by its ID
    ReactionType getReactionTypeById(UUID reactionTypeId);
}
