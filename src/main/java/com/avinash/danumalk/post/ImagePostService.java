package com.avinash.danumalk.post;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class ImagePostService {
    private final ImagePostRepository imagePostRepository;
    private final ImagePostMapper imagePostMapper; // Add the ImagePostMapper

    /**
     * Retrieves all the image posts from the repository and converts them into a list of ImagePostDTO objects.
     *
     * @return         	A list of ImagePostDTO objects containing the image posts.
     */
    public List<ImagePostDTO> getAllImagePosts() {
        List<ImagePost> imagePosts = imagePostRepository.findAll();
        return Collections.singletonList(imagePostMapper.imagePostToDTO((ImagePost) imagePosts)); // Use the mapper to convert to DTOs
    }

    /**
     * Retrieves the ImagePostDTO object corresponding to the given imagePostId.
     *
     * @param  imagePostId  the ID of the image post to retrieve
     * @return              the ImagePostDTO object corresponding to the imagePostId,
     *                      or null if the image post does not exist
     */
    public ImagePostDTO getImagePostById(Long imagePostId) {
        ImagePost imagePost = imagePostRepository.findById(imagePostId).orElse(null);
        if (imagePost != null) {
            return imagePostMapper.imagePostToDTO(imagePost); // Use the mapper to convert to DTO
        }
        return null;
    }

    /**
     * Creates a new image post using the provided image post DTO.
     *
     * @param  imagePostDTO  the image post DTO to be created
     * @return               the created image post DTO
     */
    public ImagePostDTO createImagePost(ImagePostDTO imagePostDTO) {
        ImagePost imagePost = imagePostMapper.dtoToImagePost(imagePostDTO); // Use the mapper to convert to an entity
        ImagePost savedImagePost = imagePostRepository.save(imagePost);
        return imagePostMapper.imagePostToDTO(savedImagePost); // Use the mapper to convert back to DTO
    }

    /**
     * Updates an image post with the given ID.
     *
     * @param  imagePostId           the ID of the image post to update
     * @param  updatedImagePostDTO   the updated image post DTO
     * @return                       the updated image post DTO
     */
    public ImagePostDTO updateImagePost(Long imagePostId, ImagePostDTO updatedImagePostDTO) {
        if (imagePostRepository.existsById(imagePostId)) {
            ImagePost imagePost = imagePostMapper.dtoToImagePost(updatedImagePostDTO); // Use the mapper to convert to an entity
            imagePost.setPostId(imagePostId);
            ImagePost savedImagePost = imagePostRepository.save(imagePost);
            return imagePostMapper.imagePostToDTO(savedImagePost); // Use the mapper to convert back to DTO
        }
        return null; // ImagePost not found
    }

    /**
     * Deletes an image post with the specified ID.
     *
     * @param  imagePostId  the ID of the image post to delete
     * @return              true if the image post was deleted successfully, false otherwise
     */
    public boolean deleteImagePost(Long imagePostId) {
        if (imagePostRepository.existsById(imagePostId)) {
            imagePostRepository.deleteById(imagePostId);
            return true;
        }
        return false; // ImagePost not found
    }


}