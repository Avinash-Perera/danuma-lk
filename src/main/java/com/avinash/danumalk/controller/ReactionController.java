package com.avinash.danumalk.controller;

import com.avinash.danumalk.dto.ReactionDTO;
import com.avinash.danumalk.model.Dislike;
import com.avinash.danumalk.model.Like;
import com.avinash.danumalk.model.Post;
import com.avinash.danumalk.model.Reaction;
import com.avinash.danumalk.service.PostService;
import com.avinash.danumalk.service.ReactionServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/reactions")
@AllArgsConstructor
public class ReactionController {
    private final ReactionServiceImpl reactionServiceImpl;
    private final PostService postService;


    // Create a Like reaction for a post
    @PostMapping("/like/{postId}")
    public ResponseEntity<ReactionDTO> createLikeReaction(@PathVariable Long postId) {
        Optional<Post> postOptional = postService.findById(postId);

        if (postOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Like like = new Like();
        like.setPost(postOptional.get());
        Reaction addedReaction = reactionServiceImpl.addReaction(like);

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
        Reaction addedReaction = reactionServiceImpl.addReaction(dislike);

        // Create a ReactionDTO for the response
        ReactionDTO reactionDTO = new ReactionDTO();
        reactionDTO.setId(addedReaction.getId());
        reactionDTO.setPostId(postId);

        return ResponseEntity.ok(reactionDTO);
    }


    // Remove a reaction by its ID
    @DeleteMapping("/{reactionId}")
    public void removeReaction(@PathVariable Long reactionId) {
        reactionServiceImpl.removeReaction(reactionId);
    }

    @GetMapping("/likes/{postId}")
    public ResponseEntity<?> getTotalLikesForPost(@PathVariable Long postId) {
        int totalLikes = reactionServiceImpl.getTotalLikesForPost(postId);

        if (totalLikes < 0) {
            return ResponseEntity.notFound().build(); // Return 404 Not Found if post not found
        }

        return new ResponseEntity<>(totalLikes, HttpStatus.OK);
    }

    @GetMapping("/dislikes/{postId}")
    public ResponseEntity<?> getTotalDislikesForPost(@PathVariable Long postId) {
        int totalDislikes = reactionServiceImpl.getTotalDislikesForPost(postId);

        if (totalDislikes < 0) {
            return ResponseEntity.notFound().build(); // Return 404 Not Found if post not found
        }

        return new ResponseEntity<>(totalDislikes, HttpStatus.OK);
    }
}