package com.avinash.danumalk.posts.image_post;

import com.avinash.danumalk.common.PageResponse;
import com.avinash.danumalk.common.ResultResponse;
import org.springframework.security.core.Authentication;

import java.util.UUID;

public interface InterfaceImagePostService  {
    ResultResponse<ImagePostResponse> create(ImagePostRequest post, Authentication connectedUser);
    ResultResponse<ImagePostResponse> update(UUID id, ImagePostRequest post);
    boolean delete(UUID id);
    ResultResponse<ImagePostResponse> getById(UUID id);
    public PageResponse<ImagePostResponse> getAll(int page, int size);
}
