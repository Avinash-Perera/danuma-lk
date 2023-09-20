package com.avinash.danumalk.controller;

import com.avinash.danumalk.dto.TextPostDTO;
import com.avinash.danumalk.model.PostType;
import com.avinash.danumalk.service.TextPostService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/text")
@CrossOrigin
@AllArgsConstructor
@Validated // Enable validation for this controller
public class TextPostController {
    private final TextPostService textPostService;

    /**
     * Creates a new TextPostDTO by processing the given TextPostDTO object.
     *
     * @param  textPostDTO   the TextPostDTO object to be processed
     * @return               the created TextPostDTO object
     */
    @PostMapping
    public TextPostDTO createTextPost(@RequestBody @Valid TextPostDTO textPostDTO) {
        if (!textPostDTO.getPostType().equals(PostType.TEXT)) {
            throw new IllegalArgumentException("Invalid post type for TextPost.");
        }
        return textPostService.createTextPost(textPostDTO);
    }


    /**
     * Updates a TextPost by ID.
     *
     * @param textPostId         The ID of the TextPost to update.
     * @param updatedTextPostDTO The updated TextPostDTO object.
     * @return The updated TextPostDTO object.
     * @throws IllegalArgumentException if the provided ID does not match the post type or if the post type is changed.
     */
    // Update TextPost by ID
    @PutMapping("/{textPostId}")
    public TextPostDTO updateTextPost(@PathVariable Long textPostId, @RequestBody @Valid  TextPostDTO updatedTextPostDTO) {
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

    /**
     * Delete a TextPost by ID.
     *
     * @param textPostId The ID of the TextPost to delete.
     * @return true if the TextPost is successfully deleted, false otherwise.
     */
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
