package com.avinash.danumalk.Reactions.Reaction;

import com.avinash.danumalk.posts.BasePostEntity;
import com.avinash.danumalk.user.User;
import org.springframework.security.core.Authentication;

import java.util.UUID;

public interface ReactionService {

    void reactToPost(UUID reactionTypeId, UUID basePostId, Authentication connectedUser);

    void unReactFromPost(UUID reactionTypeId, UUID basePostId, Authentication connectedUser);
}
