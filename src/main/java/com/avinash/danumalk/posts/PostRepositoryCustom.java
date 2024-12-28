package com.avinash.danumalk.posts;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PostRepositoryCustom {
    Page<PostsResponse> getAllPostsSortedByDate(Pageable pageable, UUID userId);
    Page<PostsResponse> getAllPostsByUserIdSortedByDate(Pageable pageable, UUID userId);
}
