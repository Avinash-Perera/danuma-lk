package com.avinash.danumalk.service;

import com.avinash.danumalk.model.ImagePost;
import com.avinash.danumalk.repository.ImagePostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImagePostService {

    @Autowired
    private ImagePostRepository imagePostRepository;

    public List<ImagePost> getAllImagePosts() {
        return imagePostRepository.findAll();
    }

    public ImagePost getImagePostById(Long imagePostId) {
        return imagePostRepository.findById(imagePostId).orElse(null);
    }

    public ImagePost createImagePost(ImagePost imagePost) {
        return imagePostRepository.save(imagePost);
    }

    public ImagePost updateImagePost(Long imagePostId, ImagePost updatedImagePost) {
        if (imagePostRepository.existsById(imagePostId)) {
            updatedImagePost.setPostId(imagePostId);
            return imagePostRepository.save(updatedImagePost);
        }
        return null; // ImagePost not found
    }

    public boolean deleteImagePost(Long imagePostId) {
        if (imagePostRepository.existsById(imagePostId)) {
            imagePostRepository.deleteById(imagePostId);
            return true;
        }
        return false; // ImagePost not found
    }
}
