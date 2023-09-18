package com.avinash.danumalk.service;

import com.avinash.danumalk.model.Reaction;

public interface IReactionService {
    Reaction addReaction(Reaction reaction);

    void removeReaction(Long reactionId);

    int getTotalLikesForPost(Long postId);

    int getTotalDislikesForPost(Long postId);
}
