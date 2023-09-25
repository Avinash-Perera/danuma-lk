package com.avinash.danumalk.service;

import com.avinash.danumalk.model.Reaction;
import com.avinash.danumalk.repository.ReactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReactionServiceImpl implements IReactionService  {
    private final  ReactionRepository reactionRepository;

    /**
     * Adds a reaction to the database.
     *
     * @param  reaction  the reaction object to be added
     * @return           the saved reaction object
     */
    @Override
    public Reaction addReaction(Reaction reaction) {
        // Implement logic to add a reaction to the database
        return reactionRepository.save(reaction);
    }

    /**
     * Removes a reaction from the database.
     *
     * @param  reactionId  the ID of the reaction to be removed
     */
    @Override
    public void removeReaction(Long reactionId) {
        // Implement logic to remove a reaction from the database
        reactionRepository.deleteById(reactionId);
    }

    /**
     * Retrieves the total number of likes for a given post.
     *
     * @param  postId  the unique identifier of the post
     * @return         the total number of likes for the post
     */
    @Override
    public int getTotalLikesForPost(Long postId) {
        return reactionRepository.countLikesByPostId(postId);
    }

    /**
     * Returns the total number of dislikes for a given post.
     *
     * @param  postId  the ID of the post
     * @return         the total number of dislikes for the post
     */
    @Override
    public int getTotalDislikesForPost(Long postId) {
        return reactionRepository.countDislikesByPostId(postId);
    }



}