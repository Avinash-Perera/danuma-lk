package com.avinash.danumalk.posts.image_post;

import com.avinash.danumalk.Reactions.Reaction.Reaction;
import com.avinash.danumalk.posts.PostType;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface ImagePostRepositoryCustom {
    ImagePost saveAndFetchWithPostType(ImagePost imagePost, UUID postTypeId, List<UUID> postCategoryIds);
    ImagePostResponse getImagePostDetails(UUID postId);
    ImagePostResponse updateImagePost(ImagePost imagePost, List<UUID> postCategoryIds);
    Page<ImagePostResponse> getPaginatedImagePostsWithReactions(int page, int size, UUID userId);
}
