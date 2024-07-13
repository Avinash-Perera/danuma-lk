package com.avinash.danumalk.reactions;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/reactions")
@AllArgsConstructor
public class ReactionController {
    private final ReactionService reactionService;


    @PostMapping("/like")
    public ResponseEntity<LikeReactionResponseDTO> addLikeReaction(
            @RequestParam String key,
            @RequestParam Long postId
    ) {
        LikeReactionResponseDTO addedReaction = reactionService.addLikeReaction(key, postId);
        return new ResponseEntity<>(addedReaction, HttpStatus.CREATED);
    }

    @DeleteMapping("/like/{likeId}")
    public ResponseEntity<String> removeLikeReaction(@PathVariable Long likeId) {
        try {
            reactionService.removeLikeReaction(likeId);
            return ResponseEntity.ok("Like reaction with ID " + likeId + " has been deleted");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


}
