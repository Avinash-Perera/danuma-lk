package com.avinash.danumalk.Reactions.Reaction;

import com.avinash.danumalk.posts.BasePostEntity;
import com.avinash.danumalk.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class LikeReactionStrategy implements ReactionStrategy {

    private final ReactionRepository reactionRepository;

    @Override
    public void react(BasePostEntity post, User user, Reaction reaction) {
        if (reactionRepository.findByPostAndOwnerAndReactionType(post, user, reaction.getReactionType()).isEmpty()) {
            reaction.setPost(post);
            reaction.setOwner(user);
            reactionRepository.save(reaction);
        }
    }

    @Override
    public void unReact(BasePostEntity post, User user, Reaction reaction) {
        reactionRepository.deleteByPostAndOwnerAndReactionType(post, user, reaction.getReactionType());
    }
}
