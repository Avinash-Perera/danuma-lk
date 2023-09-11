package com.avinash.danumalk.controller;

import com.avinash.danumalk.model.Dislike;
import com.avinash.danumalk.model.Like;
import com.avinash.danumalk.model.Post;
import com.avinash.danumalk.model.Reaction;
import com.avinash.danumalk.service.reactions.ReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reactions")
public class ReactionController {

    @Autowired
    private ReactionService reactionService;
    // Create a Like reaction for a post

    @PostMapping("/like/{postId}")
    public Reaction createLikeReaction(@PathVariable Long postId) {
        Like like = new Like();
        like.setPost(new Post(postId)); // Assuming you have a Post class with a constructor that takes postId
        return reactionService.addReaction(like);
    }

    // Create a Dislike reaction for a post
    @PostMapping("/dislike/{postId}")
    public Reaction createDislikeReaction(@PathVariable Long postId) {
        Dislike dislike = new Dislike();
        dislike.setPost(new Post(postId)); // Assuming you have a Post class with a constructor that takes postId
        return reactionService.addReaction(dislike);
    }

    // Remove a reaction by its ID
    @DeleteMapping("/{reactionId}")
    public void removeReaction(@PathVariable Long reactionId) {
        reactionService.removeReaction(reactionId);
    }
}
