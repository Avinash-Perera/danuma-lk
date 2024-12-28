package com.avinash.danumalk.posts;

import org.springframework.stereotype.Component;

@Component
public class PostCategoryMapper {

    public PostCategoryResponse mapToPostCategoryResponse(PostCategory postCategory) {
        return PostCategoryResponse.builder()
                .id(postCategory.getId())
                .categoryName(postCategory.getCategoryName())
                .key(postCategory.getKey())
                .build();
    }


}
