package com.avinash.danumalk.Reactions.ReactionType;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ReactionTypeServiceImpl implements ReactionTypeService{

    private final ReactionTypeRepository reactionTypeRepository;


    @Override
    public List<ReactionType> getAllReactionTypes() {
        return reactionTypeRepository.findAll();
    }

    @Override
    public ReactionType getReactionTypeById(UUID reactionTypeId) {
        return reactionTypeRepository.findById(reactionTypeId)
                .orElseThrow(() -> new EntityNotFoundException("ReactionType not found with ID: " + reactionTypeId));
    }
}

