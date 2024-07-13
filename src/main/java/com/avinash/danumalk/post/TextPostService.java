package com.avinash.danumalk.post;

import com.avinash.danumalk.exceptions.UnauthorizedAccessException;
import com.avinash.danumalk.exceptions.handleInvalidPostTypeException;
import com.avinash.danumalk.util.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class TextPostService implements TextPostServiceInterface {
    private final TextPostRepository textPostRepository;
    private final TextPostMapper textPostMapper; // Add the TextPostMapper
    private final SecurityUtils securityUtils;

    @Override
    public List<TextPostDTO> getAllTextPosts() {
        List<TextPost> textPosts = textPostRepository.findAll();
        return Collections.singletonList(textPostMapper.textPostToDTO((TextPost) textPosts)); // Use the mapper to convert to DTOs
    }

    @Override
    public TextPostDTO getTextPostById(Long textPostId) {
        TextPost textPost = textPostRepository.findById(textPostId).orElse(null);
        if (textPost != null) {
            return textPostMapper.textPostToDTO(textPost); // Use the mapper to convert to DTO
        }
        return null;
    }

    @Override
    public TextPostDTO createTextPost(TextPostDTO textPostDTO) {

        if (!textPostDTO.getPostType().equals(PostType.TEXT)) {
            throw new IllegalArgumentException("Invalid post type for TEXT.");
        }
        // setting authenticated users id
        Integer ConnectedUserId = securityUtils.getAuthenticatedUserId();
        System.out.println(ConnectedUserId);
        textPostDTO.setUserId(ConnectedUserId);
        var textPost = textPostMapper.dtoToTextPost(textPostDTO);
        textPost.getUser().setId(textPostDTO.getUserId());
        var savedTextPost = textPostRepository.save(textPost);
        return textPostMapper.textPostToDTO(savedTextPost);
    }

    @Override
    public TextPostDTO updateTextPost(Long textPostId, TextPostDTO updatedTextPostDTO) {

        if (updatedTextPostDTO.getPostType() != PostType.TEXT) {
            throw new handleInvalidPostTypeException("Cannot change the post type for TEXT.");
        }
        var existingTextPost = textPostRepository.findById(textPostId).orElse(null);
        if (existingTextPost == null) {
            throw new IllegalArgumentException("TEXT post not found.");
        }
        Integer authenticatedUserId = securityUtils.getAuthenticatedUserId();

        // Check if the authenticated user created the post
        if (!existingTextPost.getUser().getId().equals(authenticatedUserId)) {
            throw new UnauthorizedAccessException("Unauthorized to update this TEXT post.");
        }
        existingTextPost.setTitle(updatedTextPostDTO.getTitle());
        existingTextPost.setContent(updatedTextPostDTO.getContent());
        var savedTextPost = textPostRepository.save(existingTextPost);
        System.out.println(savedTextPost);
        return textPostMapper.textPostToDTO(savedTextPost);
    }



    public boolean deleteTextPost(Long textPostId) {
        Integer authenticatedUserId = securityUtils.getAuthenticatedUserId();

        // Check if the authenticated user created the post
        var existingTextPost = textPostRepository.findById(textPostId).orElse(null);
        if (existingTextPost != null && existingTextPost.getUser().getId().equals(authenticatedUserId)) {
            // Check if the provided ID matches the post type
            if (existingTextPost.getPostType() != PostType.TEXT) {
                throw new IllegalArgumentException("Invalid post type for TEXT.");
            }

            textPostRepository.deleteById(textPostId);
            return true;
        }else {
            throw new UnauthorizedAccessException("Unauthorized to delete this image post or post not found.");
        }

    }
}