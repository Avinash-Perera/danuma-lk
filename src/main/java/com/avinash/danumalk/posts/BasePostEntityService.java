package com.avinash.danumalk.posts;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;

public interface BasePostEntityService {
    Page<PostsResponse> getAllPostsSortedByDate(Pageable pageable,  @Nullable Authentication connectedUser);
}
