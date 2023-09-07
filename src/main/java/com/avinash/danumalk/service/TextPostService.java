package com.avinash.danumalk.service;

import com.avinash.danumalk.model.ImagePost;
import com.avinash.danumalk.model.TextPost;
import com.avinash.danumalk.repository.TextPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TextPostService {
    @Autowired
    private PostService postService;

    @Autowired
    private TextPostRepository textPostRepository;

    public List<TextPost> getAllTextPosts() {
        return textPostRepository.findAll();
    }

    public TextPost getTextPostById(Long textPostId) {
        return textPostRepository.findById(textPostId).orElse(null);
    }

    public TextPost createTextPost(TextPost textPost) {
        return (TextPost) postService.createPost(textPost);
    }

    public TextPost updateTextPost(Long textPostId, TextPost updatedTextPost) {
        if (textPostRepository.existsById(textPostId)) {
            updatedTextPost.setPostId(textPostId);
            TextPost savedTextPost = textPostRepository.save(updatedTextPost);
            // Update the post in the main post table and return the updated object
            return (TextPost) postService.updatePost(textPostId, savedTextPost);
        }
        return null; // TextPost not found
    }



    public boolean deleteTextPost(Long textPostId) {
        // Call the common deletePost method from PostService
        return postService.deletePost(textPostId);
    }
}
