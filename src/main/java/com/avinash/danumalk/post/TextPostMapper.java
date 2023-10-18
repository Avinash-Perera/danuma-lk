package com.avinash.danumalk.post;

import com.avinash.danumalk.post.TextPost;
import com.avinash.danumalk.post.TextPostDTO;
import org.springframework.stereotype.Component;

@Component
public class TextPostMapper {

    /**
     * Generates a TextPostDTO object from a TextPost object.
     *
     * @param  textPost  the TextPost object to convert
     * @return           the TextPostDTO object generated
     */
    public TextPostDTO textPostToDTO(TextPost textPost) {
        TextPostDTO textPostDTO = mapToDTO(textPost);
        textPostDTO.setContent(textPost.getContent());
        return textPostDTO;
    }

    /**
     * Converts a TextPostDTO object to a TextPost object.
     *
     * @param  textPostDTO  the TextPostDTO object to convert
     * @return              the converted TextPost object
     */
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
