package com.avinash.danumalk.service.reactions;

import com.avinash.danumalk.model.Reaction;
import com.avinash.danumalk.repository.ReactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReactionService {
    @Autowired
    private ReactionRepository reactionRepository;

    public Reaction addReaction(Reaction reaction) {
        // Implement logic to add a reaction to the database
        return reactionRepository.save(reaction);
    }

    public void removeReaction(Long reactionId) {
        // Implement logic to remove a reaction from the database
        reactionRepository.deleteById(reactionId);
    }

    public List<Reaction> getAllReactionsForPost(Long postId) {
        // Implement logic to retrieve all reactions for a specific post
        return reactionRepository.findAllByPostPostId(postId);
    }


}
