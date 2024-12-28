package com.avinash.danumalk.Reactions.Reaction;

import com.avinash.danumalk.posts.BasePostEntity;
import com.avinash.danumalk.user.User;

import java.util.UUID;

public interface ReactionStrategy {
    void react(BasePostEntity post, User user, Reaction reaction);
    void unReact(UUID reactionId);
}
