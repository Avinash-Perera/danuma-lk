package com.avinash.danumalk.reaction;

import com.avinash.danumalk.post.Post;
import com.avinash.danumalk.post.PostServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/reactions")
@AllArgsConstructor
public class ReactionController {
    private final ReactionServiceImpl reactionServiceImpl;
    private final PostServiceImpl postService;


    /**
     * Create a like reaction for a post.
     *
     * @param  postId  the ID of the post to create the like reaction for
     * @return         the created like reaction as a ReactionDTO
     */
    @PostMapping("/like/{postId}")
    public ResponseEntity<ReactionDTO> createLikeReaction(@PathVariable Long postId) {
        Optional<Post> postOptional = postService.findById(postId);

        if (postOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Like like = new Like();
        like.setPost(postOptional.get());
        Reaction addedReaction = reactionServiceImpl.addReaction(like);

        // Create a ReactionDTO for the response
        ReactionDTO reactionDTO = new ReactionDTO();
        reactionDTO.setId(addedReaction.getId());
        reactionDTO.setPostId(postId);

        return ResponseEntity.ok(reactionDTO);
    }

    /**
     * Creates a dislike reaction for a specific post.
     *
     * @param  postId  the ID of the post to create the dislike reaction for
     * @return         the created ReactionDTO for the response
     */
    @PostMapping("/dislike/{postId}")
    public ResponseEntity<ReactionDTO> createDislikeReaction(@PathVariable Long postId) {
        Optional<Post> postOptional = postService.findById(postId);

        if (postOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Dislike dislike = new Dislike();
        dislike.setPost(postOptional.get());
        Reaction addedReaction = reactionServiceImpl.addReaction(dislike);

        // Create a ReactionDTO for the response
        ReactionDTO reactionDTO = new ReactionDTO();
        reactionDTO.setId(addedReaction.getId());
        reactionDTO.setPostId(postId);

        return ResponseEntity.ok(reactionDTO);
    }


    /**
     * Deletes a reaction with the given reaction ID.
     *
     * @param  reactionId  the ID of the reaction to be removed
     */
    @DeleteMapping("/{reactionId}")
    public void removeReaction(@PathVariable Long reactionId) {
        reactionServiceImpl.removeReaction(reactionId);
    }

    /**
     * Retrieves the total number of likes for a specific post.
     *
     * @param  postId  the ID of the post
     * @return         a ResponseEntity object containing the total number of likes for the post
     */
    @GetMapping("/likes/{postId}")
    public ResponseEntity<?> getTotalLikesForPost(@PathVariable Long postId) {
        int totalLikes = reactionServiceImpl.getTotalLikesForPost(postId);

        if (totalLikes < 0) {
            return ResponseEntity.notFound().build(); // Return 404 Not Found if post not found
        }

        return new ResponseEntity<>(totalLikes, HttpStatus.OK);
    }

    /**
     * Retrieves the total number of dislikes for a specific post.
     *
     * @param  postId  the ID of the post
     * @return         the total number of dislikes for the post
     */
    @GetMapping("/dislikes/{postId}")
    public ResponseEntity<?> getTotalDislikesForPost(@PathVariable Long postId) {
        int totalDislikes = reactionServiceImpl.getTotalDislikesForPost(postId);

        if (totalDislikes < 0) {
            return ResponseEntity.notFound().build(); // Return 404 Not Found if post not found
        }

        return new ResponseEntity<>(totalDislikes, HttpStatus.OK);
    }
}