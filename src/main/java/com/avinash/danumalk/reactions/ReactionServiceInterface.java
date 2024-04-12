package com.avinash.danumalk.reactions;


public interface ReactionServiceInterface {

    LikeReactionResponseDTO addLikeReaction(String key, long postId );
    void removeLikeReaction(Long likeId);

}
