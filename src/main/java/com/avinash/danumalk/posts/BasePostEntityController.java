package com.avinash.danumalk.posts;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

@RestController
@RequiredArgsConstructor
@RequestMapping("posts")
public class BasePostEntityController {

    private final BasePostEntityService basePostEntityService;

    @GetMapping
    public Page<PostsResponse> getPosts(
            @RequestParam int page,
            @RequestParam int size,
            @Nullable Authentication authentication) {
        Pageable pageable = PageRequest.of(page, size);
        return basePostEntityService.getAllPostsSortedByDate(pageable, authentication);
    }

}