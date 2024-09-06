package com.avinash.danumalk.Reactions.Reaction;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/reactions")
@AllArgsConstructor
public class ReactionController {

    private final ReactionService reactionService;

    @PostMapping("/react")
    public ResponseEntity<String> reactToPost(
            @RequestParam UUID reactionTypeId, // Changed from String to UUID
            @RequestParam UUID basePostId,
            Authentication authentication) {

        reactionService.reactToPost(reactionTypeId, basePostId, authentication); // Updated method call
        return ResponseEntity.ok("Reaction added successfully");
    }


    @PostMapping("/unReact")
    public ResponseEntity<String> unReactFromPost(
            @RequestParam UUID reactionTypeId,
            @RequestParam UUID basePostId,
            Authentication authentication) {

        reactionService.unReactFromPost(reactionTypeId, basePostId, authentication);
        return ResponseEntity.ok("Reaction removed successfully");
    }
}
