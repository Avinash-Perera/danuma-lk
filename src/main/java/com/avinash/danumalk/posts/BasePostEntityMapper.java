package com.avinash.danumalk.posts;

import com.avinash.danumalk.posts.image_post.ImagePost;
import com.avinash.danumalk.posts.image_post.ImagePostMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class BasePostEntityMapper {
    private final ImagePostMapper imagePostMapper;

    public BasePostEntityResponse mapToBasePostEntity(BasePostEntity basePostEntity,long reactionCount, long commentCount, boolean isUserReacted) {
        BasePostEntityResponse basePostEntityResponse;

        if (basePostEntity instanceof ImagePost) {
            basePostEntityResponse = imagePostMapper.mapToImagePostResponse((ImagePost) basePostEntity, reactionCount, commentCount ,isUserReacted);
        } else {
            // Handle other types of BasePostEntity if needed
            throw new IllegalArgumentException("Unsupported BasePostEntity type");
        }

        return basePostEntityResponse;
    }
}
