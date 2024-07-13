package com.avinash.danumalk.reactions;


public interface ReactionServiceInterface {

    LikeReactionResponseDTO addLikeReaction(String key, long postId );
    boolean hasUserLikedPost(Integer userId, Long postId);
    void removeLikeReaction(Long likeId);

}
