package com.avinash.danumalk.service;

import com.avinash.danumalk.model.ImagePost;
import com.avinash.danumalk.repository.ImagePostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImagePostService {
    @Autowired
    private PostService postService;

    @Autowired
    private ImagePostRepository imagePostRepository;

    public List<ImagePost> getAllImagePosts() {
        return imagePostRepository.findAll();
    }

    public ImagePost getImagePostById(Long imagePostId) {
        return imagePostRepository.findById(imagePostId).orElse(null);
    }

    public ImagePost createImagePost(ImagePost imagePost) {
        return (ImagePost) postService.createPost(imagePost);
    }

    public ImagePost updateImagePost(Long imagePostId, ImagePost updatedImagePost) {
        if (imagePostRepository.existsById(imagePostId)) {
            updatedImagePost.setPostId(imagePostId);
            ImagePost savedImagePost = imagePostRepository.save(updatedImagePost);
            // Update the post in the main post table and return the updated object
            return (ImagePost) postService.updatePost(imagePostId, savedImagePost);
        }
        return null; // ImagePost not found
    }

    public boolean deleteImagePost(Long imagePostId) {
        // Call the common deletePost method from PostService
        return postService.deletePost(imagePostId);
    }

}
