package com.avinash.danumalk.Reactions.Reaction;

import com.avinash.danumalk.posts.BasePostEntity;
import com.avinash.danumalk.user.User;

public interface ReactionStrategy {
    void react(BasePostEntity post, User user, Reaction reaction);
    void unReact(BasePostEntity post, User user, Reaction reaction);
}
