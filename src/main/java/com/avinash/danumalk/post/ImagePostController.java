package com.avinash.danumalk.post;

import com.avinash.danumalk.auth.AuthenticationService;
import com.avinash.danumalk.exceptions.UnauthorizedAccessException;
import com.avinash.danumalk.exceptions.handleInvalidPostTypeException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/image")
@CrossOrigin
@AllArgsConstructor
@Validated // Enable validation for this controller

public class ImagePostController {
    private final ImagePostService imagePostService;
    private final AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<?> createImagePost(@RequestBody @Valid ImagePostDTO imagePostDTO) {
        try {
            var createdImagePostDTO = imagePostService.createImagePost(imagePostDTO);
            return ResponseEntity.ok(createdImagePostDTO); // Return success response
        } catch (IllegalArgumentException e) {
            // Handle invalid post type exception
            return ResponseEntity.badRequest().body(e.getMessage()); // Return bad request response with null body
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{imagePostId}")
    public ResponseEntity<?> updateImagePost(@PathVariable Long imagePostId, @RequestBody @Valid ImagePostDTO updatedImagePostDTO) {
        try {
            var updatedPostDTO = imagePostService.updateImagePost(imagePostId, updatedImagePostDTO);
            return ResponseEntity.ok(updatedPostDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch( handleInvalidPostTypeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        } catch (UnauthorizedAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage()); // Return unauthorized response with null body
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage()); // Return internal server error response with null body
        }
    }


    @DeleteMapping("/{imagePostId}")
    public ResponseEntity<?> deleteImagePost(@PathVariable Long imagePostId) {
        // Attempt to delete the image post
        try {
            boolean deleted = imagePostService.deleteImagePost(imagePostId);
            if (deleted) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (UnauthorizedAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
