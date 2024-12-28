package com.avinash.danumalk.Reactions.Reaction;

import com.avinash.danumalk.Reactions.ReactionType.ReactionType;
import com.avinash.danumalk.Reactions.ReactionType.ReactionTypeRepository;
import com.avinash.danumalk.notification.NotificationService;
import com.avinash.danumalk.posts.BasePostEntity;
import com.avinash.danumalk.posts.BasePostEntityRepository;
import com.avinash.danumalk.user.User;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ReactionServiceImpl implements ReactionService {

    private final ReactionRepository reactionRepository;
    private final BasePostEntityRepository postRepository;
    private final ReactionTypeRepository reactionTypeRepository;
    private final Map<UUID, ReactionStrategy> strategyMap; // Change to use UUID
    private final NotificationService notificationService;

    @Override
    @Transactional
    public void reactToPost(UUID reactionTypeId, UUID basePostId, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        BasePostEntity post = postRepository.findById(basePostId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        ReactionType reactionType = reactionTypeRepository.findById(reactionTypeId)
                .orElseThrow(() -> new IllegalArgumentException("ReactionType not found"));

        Reaction reaction = new Reaction();
        reaction.setReactionType(reactionType);

        // Log strategy map and reaction type ID
        System.out.println("Strategy Map Contents: " + strategyMap);
        System.out.println("Reaction Type ID: " + reactionTypeId);

        ReactionStrategy strategy = strategyMap.get(reactionTypeId);
        System.out.println("Retrieved Strategy: " + strategy);

        if (strategy != null) {
            strategy.react(post, user, reaction);
        } else {
            throw new IllegalArgumentException("No strategy found for reaction type ID: " + reactionTypeId);
        }

//        if (!post.getOwner().equals(user)) { // Prevent notification if the user reacts to their own post
//            String message = user.getUsername() + " liked your post!";
//            notificationService.createNotification(post.getOwner(), post, message);
//        }
    }

    @Override
    public void unReactFromPost(UUID reactionTypeId, UUID basePostId, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        BasePostEntity post = postRepository.findById(basePostId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        ReactionType reactionType = reactionTypeRepository.findById(reactionTypeId)
                .orElseThrow(() -> new IllegalArgumentException("ReactionType not found"));

        Reaction reaction = new Reaction();
        reaction.setReactionType(reactionType);

        ReactionStrategy strategy = strategyMap.get(reactionTypeId);
        if (strategy != null) {
            strategy.unReact(reaction.getId());
        } else {
            throw new IllegalArgumentException("No strategy found for reaction: " + reactionTypeId);
        }

    }
}
