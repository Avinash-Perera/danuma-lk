package com.avinash.danumalk.dto;

import com.avinash.danumalk.model.TextPost;
import org.springframework.stereotype.Component;

@Component
public class TextPostMapper {
    public TextPostDTO textPostToDTO(TextPost textPost) {
        TextPostDTO textPostDTO = mapToDTO(textPost);
        textPostDTO.setContent(textPost.getContent());
        return textPostDTO;
    }

    public TextPost dtoToTextPost(TextPostDTO textPostDTO) {
        TextPost textPost = mapToTextPost(textPostDTO);
        textPost.setContent(textPostDTO.getContent());
        return textPost;
    }

    //Helper functions
    private TextPostDTO mapToDTO(TextPost textPost) {
        TextPostDTO textPostDTO = new TextPostDTO();
        textPostDTO.setPostId(textPost.getPostId());
        textPostDTO.setTitle(textPost.getTitle());
        textPostDTO.setPostType(textPost.getPostType());
        textPostDTO.setCreatedAt(textPost.getCreatedAt());
        textPostDTO.setUpdatedAt(textPost.getUpdatedAt());

        return textPostDTO;
    }

    private TextPost mapToTextPost(TextPostDTO textPostDTO) {
        TextPost textPost = new TextPost();
        textPost.setPostId(textPostDTO.getPostId());
        textPost.setTitle(textPostDTO.getTitle());
        textPost.setPostType(textPostDTO.getPostType());
        textPost.setCreatedAt(textPostDTO.getCreatedAt());
        textPost.setUpdatedAt(textPostDTO.getUpdatedAt());

        return textPost;
    }
}
