package com.avinash.danumalk.reactions;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reaction-types")
@AllArgsConstructor
public class ReactionTypeController {

    private final ReactionTypeService reactionTypeService;

    @GetMapping
    public ResponseEntity<List<ReactionType>> getAllReactionTypes() {
        List<ReactionType> reactionTypes = reactionTypeService.getAllReactionTypes();
        return ResponseEntity.ok(reactionTypes);
    }

    @GetMapping("/{reactionTypeId}")
    public ResponseEntity<ReactionType> getReactionTypeById(@PathVariable UUID reactionTypeId) {
        ReactionType reactionType = reactionTypeService.getReactionTypeById(reactionTypeId);
        return ResponseEntity.ok(reactionType);
    }
}
