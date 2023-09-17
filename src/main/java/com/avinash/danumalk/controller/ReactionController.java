package com.avinash.danumalk.controller;

import com.avinash.danumalk.dto.ReactionDTO;
import com.avinash.danumalk.model.Dislike;
import com.avinash.danumalk.model.Like;
import com.avinash.danumalk.model.Post;
import com.avinash.danumalk.model.Reaction;
import com.avinash.danumalk.service.PostService;
import com.avinash.danumalk.service.ReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/reactions")
public class ReactionController {

    @Autowired
    private ReactionService reactionService;

    @Autowired
    private PostService postService;


    // Create a Like reaction for a post
    @PostMapping("/like/{postId}")
    public ResponseEntity<ReactionDTO> createLikeReaction(@PathVariable Long postId) {
        Optional<Post> postOptional = postService.findById(postId);

        if (postOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Like like = new Like();
        like.setPost(postOptional.get());
        Reaction addedReaction = reactionService.addReaction(like);

        // Create a ReactionDTO for the response
        ReactionDTO reactionDTO = new ReactionDTO();
        reactionDTO.setId(addedReaction.getId());
        reactionDTO.setPostId(postId);

        return ResponseEntity.ok(reactionDTO);
    }

    // Create a Dislike reaction for a post
    @PostMapping("/dislike/{postId}")
    public ResponseEntity<ReactionDTO> createDislikeReaction(@PathVariable Long postId) {
        Optional<Post> postOptional = postService.findById(postId);

        if (postOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Dislike dislike = new Dislike();
        dislike.setPost(postOptional.get());
        Reaction addedReaction = reactionService.addReaction(dislike);

        // Create a ReactionDTO for the response
        ReactionDTO reactionDTO = new ReactionDTO();
        reactionDTO.setId(addedReaction.getId());
        reactionDTO.setPostId(postId);

        return ResponseEntity.ok(reactionDTO);
    }


    // Remove a reaction by its ID
    @DeleteMapping("/{reactionId}")
    public void removeReaction(@PathVariable Long reactionId) {
        reactionService.removeReaction(reactionId);
    }

}