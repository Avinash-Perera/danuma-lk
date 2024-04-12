package com.avinash.danumalk.reactions;

import com.avinash.danumalk.post.PostRepository;
import com.avinash.danumalk.user.User;
import com.avinash.danumalk.util.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReactionService implements ReactionServiceInterface{
    private final  LikeReactionMapper likeReactionMapper;
    private final LikeReactionRepository likeReactionRepository;
    private final ReactionTypeRepository reactionTypeRepository;
    private final PostRepository postRepository;
    private final SecurityUtils securityUtils;

    @Override
    public LikeReactionResponseDTO addLikeReaction(String key, long postId ) {
        String lowercaseKey = key.toLowerCase();

        // Find the ReactionType by name (key)
        var reactionType = reactionTypeRepository.findByName(lowercaseKey)
                .orElseThrow(() -> new RuntimeException("ReactionType not found for key: " + key));

        var post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("ReactionType not found for key: " + key));

        var reactionTypeId = reactionType.getReactionTypeId();

        // setting authenticated users id
        Integer authenticatedUserId = securityUtils.getAuthenticatedUserId();
        LikeReaction likeReaction = new LikeReaction();
        likeReaction.setReactionType(reactionType);
        likeReaction.setPost(post);

        // Set the authenticated user's id on the LikeReaction's User
        User user = new User();
        user.setId(authenticatedUserId);
        likeReaction.setUser(user);

        var savedLikeReaction = likeReactionRepository.save(likeReaction);

        return likeReactionMapper.mapToDTO(savedLikeReaction);




    }

    @Override
    public void removeLikeReaction(Long likeId) {

    }
}
