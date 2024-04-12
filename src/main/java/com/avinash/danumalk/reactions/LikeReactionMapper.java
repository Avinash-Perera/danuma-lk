package com.avinash.danumalk.reactions;


import com.avinash.danumalk.post.Post;
import com.avinash.danumalk.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class LikeReactionMapper {
    private final ReactionTypeRepository reactionTypeRepository;
    // Map a LikeReactionDTO to a LikeReaction entity
    public LikeReaction mapToEntity(LikeReactionResponseDTO dto) {
        LikeReaction entity = new LikeReaction();
        entity.setLikeId(dto.getLikeId());

        // Set the User entity based on the userId in the DTO
        if (dto.getUserId() != null) {
            User user = new User();
            user.setId(dto.getUserId());
            entity.setUser(user);
        }

//        // Set the Post entity based on the postId in the DTO
//        if (dto.getPostId() != null) {
//            Post post = new Post();
//            post.setPostId(Long.valueOf(dto.getPostId()));
//            entity.setPost(post);
//        }

//        // Set the ReactionType entity based on the reactionTypeId in the DTO
//        if (dto.getReactionTypeId() != null) {
//            UUID reactionTypeId = dto.getReactionTypeId();
//            var reactionType = reactionTypeRepository.findById(reactionTypeId)
//                    .orElseThrow(() -> new RuntimeException("ReactionType not found for id: " + reactionTypeId));
//            entity.setReactionType(reactionType);
//        }

        return entity;
    }

    // Map a LikeReaction entity to a LikeReactionDTO
    public LikeReactionResponseDTO mapToDTO(LikeReaction entity) {
        LikeReactionResponseDTO dto = new LikeReactionResponseDTO();
        dto.setLikeId(entity.getLikeId());

        // Set the userId based on the User entity
        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
        }

//        // Set the postId based on the Post entity
//        if (entity.getPost() != null) {
//            dto.setPostId(Math.toIntExact(entity.getPost().getPostId()));
//        }

//        // Set the reactionTypeId based on the ReactionType entity
//        if (entity.getReactionType() != null) {
//            dto.setReactionTypeId(entity.getReactionType().getReactionTypeId());
//        }

        return dto;
    }
}
