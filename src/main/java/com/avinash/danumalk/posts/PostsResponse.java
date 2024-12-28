package com.avinash.danumalk.posts;

import com.avinash.danumalk.posts.image_post.ImagePostResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class PostsResponse {
    private ImagePostResponse imagePostResponse;

}
