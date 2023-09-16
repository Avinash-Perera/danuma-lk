package com.avinash.danumalk.service;

import com.avinash.danumalk.dto.ImagePostDTO;
import com.avinash.danumalk.dto.ImagePostMapper;
import com.avinash.danumalk.model.ImagePost;
import com.avinash.danumalk.repository.ImagePostRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class ImagePostService {
    private final ImagePostRepository imagePostRepository;
    private final ImagePostMapper imagePostMapper; // Add the ImagePostMapper

    public List<ImagePostDTO> getAllImagePosts() {
        List<ImagePost> imagePosts = imagePostRepository.findAll();
        return Collections.singletonList(imagePostMapper.imagePostToDTO((ImagePost) imagePosts)); // Use the mapper to convert to DTOs
    }

    public ImagePostDTO getImagePostById(Long imagePostId) {
        ImagePost imagePost = imagePostRepository.findById(imagePostId).orElse(null);
        if (imagePost != null) {
            return imagePostMapper.imagePostToDTO(imagePost); // Use the mapper to convert to DTO
        }
        return null;
    }

    public ImagePostDTO createImagePost(ImagePostDTO imagePostDTO) {
        ImagePost imagePost = imagePostMapper.dtoToImagePost(imagePostDTO); // Use the mapper to convert to an entity
        ImagePost savedImagePost = imagePostRepository.save(imagePost);
        return imagePostMapper.imagePostToDTO(savedImagePost); // Use the mapper to convert back to DTO
    }

    public ImagePostDTO updateImagePost(Long imagePostId, ImagePostDTO updatedImagePostDTO) {
        if (imagePostRepository.existsById(imagePostId)) {
            ImagePost imagePost = imagePostMapper.dtoToImagePost(updatedImagePostDTO); // Use the mapper to convert to an entity
            imagePost.setPostId(imagePostId);
            ImagePost savedImagePost = imagePostRepository.save(imagePost);
            return imagePostMapper.imagePostToDTO(savedImagePost); // Use the mapper to convert back to DTO
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