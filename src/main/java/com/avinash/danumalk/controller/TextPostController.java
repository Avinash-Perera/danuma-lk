package com.avinash.danumalk.controller;

import com.avinash.danumalk.dto.TextPostDTO;
import com.avinash.danumalk.model.Comment;
import com.avinash.danumalk.model.Post;
import com.avinash.danumalk.model.PostType;
import com.avinash.danumalk.model.TextPost;
import com.avinash.danumalk.service.TextPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/text")
@CrossOrigin
public class TextPostController {

    @Autowired
    private TextPostService textPostService;

    @PostMapping
    public TextPostDTO createTextPost(@RequestBody TextPostDTO textPostDTO) {
        if (textPostDTO.getPostType() != PostType.TEXT) {
            throw new IllegalArgumentException("Invalid post type for TextPost.");
        }
        return textPostService.createTextPost(textPostDTO);
    }

    // Update TextPost by ID
    @PutMapping("/{textPostId}")
    public TextPostDTO updateTextPost(@PathVariable Long textPostId, @RequestBody TextPostDTO updatedTextPostDTO) {
        TextPostDTO existingTextPostDTO = textPostService.getTextPostById(textPostId);
        if (existingTextPostDTO != null) {
            // Check if the provided ID matches the post type
            if (existingTextPostDTO.getPostType() != PostType.TEXT) {
                throw new IllegalArgumentException("Invalid post type for TextPost.");
            }

            // Check if the PostType in the updated post matches the existing PostType
            if (updatedTextPostDTO.getPostType() != PostType.TEXT) {
                throw new IllegalArgumentException("Cannot change the post type for TextPost.");
            }



            // Update other properties of the existingTextPostDTO as needed
            existingTextPostDTO.setTitle(updatedTextPostDTO.getTitle());
            existingTextPostDTO.setContent(updatedTextPostDTO.getContent());

            // Save the updated TextPostDTO
            return textPostService.updateTextPost(textPostId, existingTextPostDTO);
        }
        return null; // TextPost not found
    }

    // Delete TextPost by ID
    @DeleteMapping("/{textPostId}")
    public boolean deleteTextPost(@PathVariable Long textPostId) {
        TextPostDTO existingTextPostDTO = textPostService.getTextPostById(textPostId);
        if (existingTextPostDTO != null) {
            // Check if the provided ID matches the post type
            if (existingTextPostDTO.getPostType() != PostType.TEXT) {
                throw new IllegalArgumentException("Invalid post type for TextPost.");
            }
            return textPostService.deleteTextPost(textPostId);
        }
        return false; // TextPost not found
    }


}
