package com.avinash.danumalk.service;

import com.avinash.danumalk.model.TextPost;
import com.avinash.danumalk.repository.TextPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TextPostService {

    @Autowired
    private TextPostRepository textPostRepository;

    public List<TextPost> getAllTextPosts() {
        return textPostRepository.findAll();
    }

    public TextPost getTextPostById(Long textPostId) {
        return textPostRepository.findById(textPostId).orElse(null);
    }

    public TextPost createTextPost(TextPost textPost) {
        return textPostRepository.save(textPost);
    }

    public TextPost updateTextPost(Long textPostId, TextPost updatedTextPost) {
        if (textPostRepository.existsById(textPostId)) {
            updatedTextPost.setPostId(textPostId);
            return textPostRepository.save(updatedTextPost);
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
