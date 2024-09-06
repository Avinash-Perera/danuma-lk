package com.avinash.danumalk.posts.image_post;

import com.avinash.danumalk.auth.AuthenticationService;
import com.avinash.danumalk.common.ErrorResponse;
import com.avinash.danumalk.common.PageResponse;
import com.avinash.danumalk.common.ResultResponse;
import com.avinash.danumalk.exceptions.UnauthorizedAccessException;
import com.avinash.danumalk.exceptions.handleInvalidPostTypeException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/posts/image")
@CrossOrigin
@AllArgsConstructor
@Validated // Enable validation for this controller

public class ImagePostController {
    private final ImagePostService imagePostService;
    private final AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<ResultResponse<ImagePostResponse>>  create(@RequestBody @Valid ImagePostRequest  post,  Authentication connectedUser) {
        try {
            var createdImagePostDTO = imagePostService.create(post,connectedUser);
            return ResponseEntity.ok(createdImagePostDTO); // Return success response
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody @Valid ImagePostRequest post) {
        try {
            var updatedImagePostDTO = imagePostService.update(id, post);
            return ResponseEntity.ok(ResultResponse.<ImagePostResponse>builder()
                    .status("SUCCESS")
                    .data(updatedImagePostDTO.getData())
                    .build()); // Return success response
        } catch (handleInvalidPostTypeException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("ERROR", e.getMessage()));
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTextPost(@PathVariable UUID id) {
        try {
            boolean deleted = imagePostService.delete(id);
            if (deleted) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        }catch (UnauthorizedAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }


    @GetMapping("/all")
    public PageResponse<ImagePostResponse> getAllPostsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return imagePostService.getAll(page, size);
    }


    @GetMapping("/{postId}")
    public ResponseEntity<ImagePostResponse> getPostById(@PathVariable UUID postId) {
        var imagePostResponse = imagePostService.getById(postId);

        if (imagePostResponse != null) {
            return ResponseEntity.ok(imagePostResponse.getData());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}

