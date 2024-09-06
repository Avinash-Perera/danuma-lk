package com.avinash.danumalk.Reactions.ReactionType;

import java.util.List;
import java.util.UUID;

public interface ReactionTypeService {
    List<ReactionType> getAllReactionTypes();
    ReactionType getReactionTypeById(UUID reactionTypeId);
}
