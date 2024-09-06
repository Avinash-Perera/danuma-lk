package com.avinash.danumalk.config;

import com.avinash.danumalk.Reactions.Reaction.LikeReactionStrategy;
import com.avinash.danumalk.Reactions.Reaction.ReactionStrategy;
import com.avinash.danumalk.Reactions.ReactionType.ReactionTypeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

@Configuration
public class ReactionConfig {

    @Bean
    public Map<UUID, ReactionStrategy> reactionStrategies(LikeReactionStrategy likeStrategy, ReactionTypeRepository reactionTypeRepository) {
        Map<UUID, ReactionStrategy> strategies = new HashMap<>();

        UUID likeReactionTypeId = reactionTypeRepository.findByKey("LIKE")
                .orElseThrow(() -> new IllegalArgumentException("ReactionType not found for key: LIKE"))
                .getReaction_type_id();

        strategies.put(likeReactionTypeId, likeStrategy);

        // Log to ensure the strategy is registered
        System.out.println("Registered Strategy for LIKE: " + likeStrategy);
        System.out.println("Strategy Map: " + strategies);

        return strategies;
    }
}
