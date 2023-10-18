package com.avinash.danumalk.post;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin
@AllArgsConstructor
public class PostController {
    private final PostService postService;

    /**
     * Returns a list of all posts.
     *
     * @return a list of PostDTO objects representing all posts
     */
    @GetMapping
    public List<PostDTO> getAllPosts() {
        return postService.getAllPosts();
    }

    /**
     * Retrieves a specific post by its ID.
     *
     * @param  postId  the ID of the post to retrieve
     * @return         the ResponseEntity containing the PostDTO if found, or a not found response if not found
     */
    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long postId) {
        PostDTO postDTO = postService.getPostById(postId);

        if (postDTO != null) {
            return ResponseEntity.ok(postDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
