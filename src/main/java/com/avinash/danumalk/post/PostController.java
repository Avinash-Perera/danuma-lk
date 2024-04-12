package com.avinash.danumalk.post;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@CrossOrigin
@AllArgsConstructor
public class PostController {
    private final PostService postService;

   
    @GetMapping
    public List<PostDTO> getAllPosts() {
        return postService.getAllPosts();
    }


    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long postId) {
        var postDTO = postService.getPostById(postId);

        if (postDTO != null) {
            return ResponseEntity.ok(postDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
