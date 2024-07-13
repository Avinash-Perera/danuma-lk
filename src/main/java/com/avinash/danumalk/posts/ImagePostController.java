package com.avinash.danumalk.posts;

import com.avinash.danumalk.auth.AuthenticationService;
import com.avinash.danumalk.common.ResultResponse;
import com.avinash.danumalk.exceptions.UnauthorizedAccessException;
import com.avinash.danumalk.exceptions.handleInvalidPostTypeException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ResultResponse<ImagePostResponse>>  create(@RequestBody @Valid ImagePostRequest  post) {
        try {
            var createdImagePostDTO = imagePostService.create(post);
            return ResponseEntity.ok(createdImagePostDTO); // Return success response
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<ResultResponse<ImagePostResponse>>  update(@PathVariable UUID id, @RequestBody @Valid ImagePostRequest  post) {
        try {
            var updatedImagePostDTO = imagePostService.update(id, post);
            return ResponseEntity.ok(updatedImagePostDTO); // Return success response
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}

