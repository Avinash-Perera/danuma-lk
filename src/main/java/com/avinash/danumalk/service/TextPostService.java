package com.avinash.danumalk.service;

import com.avinash.danumalk.dto.TextPostDTO;
import com.avinash.danumalk.dto.TextPostMapper;
import com.avinash.danumalk.model.TextPost;
import com.avinash.danumalk.repository.TextPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class TextPostService {
    @Autowired
    private PostService postService;

    @Autowired
    private TextPostRepository textPostRepository;

    @Autowired
    private TextPostMapper textPostMapper; // Add the TextPostMapper

    public List<TextPostDTO> getAllTextPosts() {
        List<TextPost> textPosts = textPostRepository.findAll();
        return Collections.singletonList(textPostMapper.textPostToDTO((TextPost) textPosts)); // Use the mapper to convert to DTOs
    }

    public TextPostDTO getTextPostById(Long textPostId) {
        TextPost textPost = textPostRepository.findById(textPostId).orElse(null);
        if (textPost != null) {
            return textPostMapper.textPostToDTO(textPost); // Use the mapper to convert to DTO
        }
        return null;
    }

    public TextPostDTO createTextPost(TextPostDTO textPostDTO) {
        TextPost textPost = textPostMapper.dtoToTextPost(textPostDTO); // Use the mapper to convert to an entity
        TextPost savedTextPost = textPostRepository.save(textPost);
        return textPostMapper.textPostToDTO(savedTextPost); // Use the mapper to convert back to DTO
    }

    public TextPostDTO updateTextPost(Long textPostId, TextPostDTO updatedTextPostDTO) {
        if (textPostRepository.existsById(textPostId)) {
            TextPost textPost = textPostMapper.dtoToTextPost(updatedTextPostDTO); // Use the mapper to convert to an entity
            textPost.setPostId(textPostId);
            TextPost savedTextPost = textPostRepository.save(textPost);
            return textPostMapper.textPostToDTO(savedTextPost); // Use the mapper to convert back to DTO
        }
        return null; // TextPost not found
    }


    public boolean deleteTextPost(Long textPostId) {
        if (textPostRepository.existsById(textPostId)) {
            textPostRepository.deleteById(textPostId);
            return true;
        }
        return false; // TextPost not found
    }
}