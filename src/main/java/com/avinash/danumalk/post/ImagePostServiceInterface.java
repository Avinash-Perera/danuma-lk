package com.avinash.danumalk.post;

import java.util.List;

public interface ImagePostServiceInterface {
    List<ImagePostDTO> getAllImagePosts();

    ImagePostDTO getImagePostById(Long imagePostId);

    ImagePostDTO createImagePost(ImagePostDTO imagePostDTO);

    ImagePostDTO updateImagePost(Long imagePostId, ImagePostDTO updatedImagePostDTO);

    boolean deleteImagePost(Long imagePostId);
}
