package com.avinash.danumalk.reaction;

import com.avinash.danumalk.reaction.Reaction;

public interface IReactionService {
    Reaction addReaction(Reaction reaction);

    void removeReaction(Long reactionId);

    int getTotalLikesForPost(Long postId);

    int getTotalDislikesForPost(Long postId);
}
