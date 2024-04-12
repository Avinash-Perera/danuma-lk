package com.avinash.danumalk.reactions;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reactions")
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


}
