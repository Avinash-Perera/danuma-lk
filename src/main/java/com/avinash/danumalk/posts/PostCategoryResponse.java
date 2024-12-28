package com.avinash.danumalk.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class PostCategoryResponse {
    private UUID id;
    private String categoryName;
    private String key;


}
