package com.avinash.danumalk.post;

import java.util.List;

public interface TextPostServiceInterface {
    List<TextPostDTO> getAllTextPosts();

    TextPostDTO getTextPostById(Long textPostId);

    TextPostDTO createTextPost(TextPostDTO textPostDTO);

    TextPostDTO updateTextPost(Long textPostId, TextPostDTO updatedTextPostDTO);

    boolean deleteTextPost(Long textPostId);
}
