package com.avinash.danumalk.posts;

import org.springframework.stereotype.Component;

@Component
public class PostTypeMapper {

    public PostTypeResponse mapToPostTypeResponse(PostType postType) {
        return PostTypeResponse.builder()
                .id(postType.getId())
                .type_name(postType.getType_name())
                .type_case_name(postType.getType_case_name())
                .build();
    }


}
