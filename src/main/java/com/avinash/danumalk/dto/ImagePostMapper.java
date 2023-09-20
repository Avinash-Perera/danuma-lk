package com.avinash.danumalk.dto;

import com.avinash.danumalk.model.ImagePost;
import org.springframework.stereotype.Component;

@Component
public class ImagePostMapper {

    /**
     * Converts an ImagePost object to an ImagePostDTO object.
     *
     * @param  imagePost  the ImagePost object to be converted
     * @return            the converted ImagePostDTO object
     */
    public ImagePostDTO imagePostToDTO(ImagePost imagePost) {
        ImagePostDTO imagePostDTO = mapToDTO(imagePost);
        imagePostDTO.setImageUrl(imagePost.getImageUrl());
        imagePostDTO.setImageDescription(imagePost.getImageDescription());
        return imagePostDTO;
    }


    /**
     * Converts an ImagePostDTO object to an ImagePost object.
     *
     * @param  imagePostDTO  the ImagePostDTO object to be converted
     * @return               the converted ImagePost object
     */
    public ImagePost dtoToImagePost(ImagePostDTO imagePostDTO) {
        ImagePost imagePost = mapToImagePost(imagePostDTO);
        imagePost.setImageUrl(imagePostDTO.getImageUrl());
        imagePost.setImageDescription(imagePostDTO.getImageDescription());
        return imagePost;
    }

    //helper methods
    private ImagePostDTO mapToDTO(ImagePost imagePost) {
        ImagePostDTO imagePostDTO = new ImagePostDTO();
        imagePostDTO.setPostId(imagePost.getPostId());
        imagePostDTO.setTitle(imagePost.getTitle());
        imagePostDTO.setPostType(imagePost.getPostType());
        imagePostDTO.setCreatedAt(imagePost.getCreatedAt());
        imagePostDTO.setUpdatedAt(imagePost.getUpdatedAt());
        return imagePostDTO;
    }

    private ImagePost mapToImagePost(ImagePostDTO imagePostDTO) {
        ImagePost imagePost = new ImagePost();
        imagePost.setPostId(imagePostDTO.getPostId());
        imagePost.setTitle(imagePostDTO.getTitle());
        imagePost.setPostType(imagePostDTO.getPostType());
        imagePost.setCreatedAt(imagePostDTO.getCreatedAt());
        imagePost.setUpdatedAt(imagePostDTO.getUpdatedAt());
        return imagePost;
    }
}
