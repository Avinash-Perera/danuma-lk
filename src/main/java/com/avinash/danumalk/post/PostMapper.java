package com.avinash.danumalk.post;
import com.avinash.danumalk.reactions.LikeReactionMapper;
import com.avinash.danumalk.reactions.LikeReactionResponseDTO;
import com.avinash.danumalk.reactions.ReactionTypeKey;
import com.avinash.danumalk.util.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class PostMapper {
    private final ImagePostMapper imagePostMapper;
    private final TextPostMapper textPostMapper;
    private final LikeReactionMapper likeReactionMapper;
    private final SecurityUtils securityUtils;



    public PostDTO postToDTO(Post post) {
        PostDTO postDTO;
        if (post instanceof ImagePost) {
            postDTO = imagePostMapper.imagePostToDTO((ImagePost) post);
        } else {
            postDTO = textPostMapper.textPostToDTO((TextPost) post);
        }

        // Map the list of LikeReactions to LikeReactionResponseDTO
        List<LikeReactionResponseDTO> likeDTOs = post.getLikeReactions().stream()
                .map(likeReactionMapper::mapToDTO)
                .collect(Collectors.toList());

        postDTO.setLikes(likeDTOs);
      // Set likeCount based on the number of likes
        postDTO.setLikeCount(post.getLikeReactions().size());
        Integer authenticatedUserId = securityUtils.getAuthenticatedUserId();
        // Check if the specified userId has liked this post
        boolean userHasLiked = post.getLikeReactions().stream()
                .anyMatch(like -> like.getUser().getId().equals(authenticatedUserId) && like.getReactionType().getKey().equals(ReactionTypeKey.LIKE.getKey()));

        postDTO.setUserLiked(userHasLiked);

        return postDTO;
    }




    public Post dtoToPost(PostDTO postDTO) {
        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setPostType(postDTO.getPostType());
        post.setCreatedAt(postDTO.getCreatedAt());
        post.setUpdatedAt(postDTO.getUpdatedAt());
        return post;
    }

}