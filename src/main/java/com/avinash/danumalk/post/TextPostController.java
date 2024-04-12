package com.avinash.danumalk.post;

import com.avinash.danumalk.exceptions.UnauthorizedAccessException;
import com.avinash.danumalk.exceptions.handleInvalidPostTypeException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/text")
@CrossOrigin
@AllArgsConstructor
@Validated // Enable validation for this controller
public class TextPostController {
    private final TextPostService textPostService;


    @PostMapping
    public ResponseEntity<?> createTextPost(@RequestBody @Valid TextPostDTO textPostDTO) {
        try {
            var createdTextPostDTO = textPostService.createTextPost(textPostDTO);
            return ResponseEntity.ok(createdTextPostDTO); // Return success response
        } catch (IllegalArgumentException e) {
            // Handle invalid post type exception
            return ResponseEntity.badRequest().body(e.getMessage()); // Return bad request response with null body
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @PutMapping("/{textPostId}")
    public ResponseEntity<?> updateTextPost(@PathVariable Long textPostId, @RequestBody @Valid TextPostDTO updatedTextPostDTO) {
        try {
            var updatedTextDTO = textPostService.updateTextPost(textPostId, updatedTextPostDTO);
            return ResponseEntity.ok(updatedTextDTO);
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



    @DeleteMapping("/{textPostId}")
    public ResponseEntity<?> deleteTextPost(@PathVariable Long textPostId) {
        try {
            boolean deleted = textPostService.deleteTextPost(textPostId);
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
