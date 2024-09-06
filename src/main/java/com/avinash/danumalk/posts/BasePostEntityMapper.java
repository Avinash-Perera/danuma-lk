package com.avinash.danumalk.posts;

import com.avinash.danumalk.posts.image_post.ImagePost;
import com.avinash.danumalk.posts.image_post.ImagePostMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BasePostEntityMapper {
    private final ImagePostMapper imagePostMapper;

    public BasePostEntityResponse mapToBasePostEntity(BasePostEntity basePostEntity, Integer authenticatedUserId) {
        BasePostEntityResponse basePostEntityResponse;

        if (basePostEntity instanceof ImagePost) {
            basePostEntityResponse = imagePostMapper.mapToImagePostResponse((ImagePost) basePostEntity);
        } else {
            // Handle other types of BasePostEntity if needed
            throw new IllegalArgumentException("Unsupported BasePostEntity type");
        }

        return basePostEntityResponse;
    }
}
