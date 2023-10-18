package com.avinash.danumalk.post;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class TextPostService {
    private final TextPostRepository textPostRepository;
    private final TextPostMapper textPostMapper; // Add the TextPostMapper

    /**
     * Retrieves all the text posts.
     *
     * @return  a list of text post DTOs
     */
    public List<TextPostDTO> getAllTextPosts() {
        List<TextPost> textPosts = textPostRepository.findAll();
        return Collections.singletonList(textPostMapper.textPostToDTO((TextPost) textPosts)); // Use the mapper to convert to DTOs
    }

    /**
     * Retrieves a TextPostDTO object by its ID.
     *
     * @param  textPostId    the ID of the TextPost to retrieve
     * @return               the corresponding TextPostDTO object, or null if not found
     */
    public TextPostDTO getTextPostById(Long textPostId) {
        TextPost textPost = textPostRepository.findById(textPostId).orElse(null);
        if (textPost != null) {
            return textPostMapper.textPostToDTO(textPost); // Use the mapper to convert to DTO
        }
        return null;
    }

    /**
     * Creates a new text post.
     *
     * @param  textPostDTO  the text post object containing the post data
     * @return              the created text post object
     */
    public TextPostDTO createTextPost(TextPostDTO textPostDTO) {
        TextPost textPost = textPostMapper.dtoToTextPost(textPostDTO); // Use the mapper to convert to an entity
        TextPost savedTextPost = textPostRepository.save(textPost);
        return textPostMapper.textPostToDTO(savedTextPost); // Use the mapper to convert back to DTO
    }

    /**
     * Updates a text post with the given ID.
     *
     * @param  textPostId            the ID of the text post to be updated
     * @param  updatedTextPostDTO    the updated text post data transfer object
     * @return                       the updated text post data transfer object if the text post exists,
     *                               null otherwise
     */
    public TextPostDTO updateTextPost(Long textPostId, TextPostDTO updatedTextPostDTO) {
        if (textPostRepository.existsById(textPostId)) {
            TextPost textPost = textPostMapper.dtoToTextPost(updatedTextPostDTO); // Use the mapper to convert to an entity
            textPost.setPostId(textPostId);
            TextPost savedTextPost = textPostRepository.save(textPost);
            return textPostMapper.textPostToDTO(savedTextPost); // Use the mapper to convert back to DTO
        }
        return null; // TextPost not found
    }

    /**
     * Deletes a text post with the given ID.
     *
     * @param  textPostId  the ID of the text post to be deleted
     * @return             true if the post is successfully deleted, false otherwise
     */
    public boolean deleteTextPost(Long textPostId) {
        if (textPostRepository.existsById(textPostId)) {
            textPostRepository.deleteById(textPostId);
            return true;
        }
        return false; // TextPost not found
    }
}