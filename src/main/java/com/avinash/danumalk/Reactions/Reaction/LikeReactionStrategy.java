package com.avinash.danumalk.Reactions.Reaction;

import com.avinash.danumalk.posts.BasePostEntity;
import com.avinash.danumalk.user.User;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

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
    @Transactional
    public void unReact(UUID reactionId) {
        reactionRepository.deleteById(reactionId);
   }
}
