package com.avinash.danumalk.service;

import com.avinash.danumalk.model.Reaction;
import com.avinash.danumalk.repository.ReactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReactionServiceImpl implements IReactionService  {
    private final  ReactionRepository reactionRepository;

    @Override
    public Reaction addReaction(Reaction reaction) {
        // Implement logic to add a reaction to the database
        return reactionRepository.save(reaction);
    }

    @Override
    public void removeReaction(Long reactionId) {
        // Implement logic to remove a reaction from the database
        reactionRepository.deleteById(reactionId);
    }

    @Override
    public int getTotalLikesForPost(Long postId) {
        return reactionRepository.countLikesByPostId(postId);
    }

    @Override
    public int getTotalDislikesForPost(Long postId) {
        return reactionRepository.countDislikesByPostId(postId);
    }



}