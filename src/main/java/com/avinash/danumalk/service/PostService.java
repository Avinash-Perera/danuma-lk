package com.avinash.danumalk.service;

import com.avinash.danumalk.model.Post;
import com.avinash.danumalk.repository.ImagePostRepository;
import com.avinash.danumalk.repository.PostRepository;
import com.avinash.danumalk.repository.TextPostRepository;
import com.avinash.danumalk.repository.VideoPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ImagePostRepository imagePostRepository;

    @Autowired
    private TextPostRepository textPostRepository;

    @Autowired
    private VideoPostRepository videoPostRepository;

    public List<Post> getAllPosts() {
        // Retrieve all posts, including subtypes, sorted by createdAt in descending order
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    public Post getPostById(Long postId) {
        return postRepository.findById(postId).orElse(null);
    }

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public Post updatePost(Long postId, Post updatedPost) {
        // Check if the post exists in any of the subtables
        if (imagePostRepository.existsById(postId)) {
            updatedPost.setPostId(postId);
            return postRepository.save(updatedPost);
        } else if (textPostRepository.existsById(postId)) {
            updatedPost.setPostId(postId);
            return postRepository.save(updatedPost);
        } else if (videoPostRepository.existsById(postId)) {
            updatedPost.setPostId(postId);
            return postRepository.save(updatedPost);
        }

        return null; // Post not found in any subtable
    }


    public boolean deletePost(Long postId) {
        // Check if the post exists in any of the subtables (e.g., image_post, text_post, video_post)
        if (imagePostRepository.existsById(postId)) {
            imagePostRepository.deleteById(postId);
            return true;
        } else if (textPostRepository.existsById(postId)) {
            textPostRepository.deleteById(postId);
            return true;
        } else if (videoPostRepository.existsById(postId)) {
            videoPostRepository.deleteById(postId);
            return true;
        }

        // If not found in any subtable, check the main post table
        if (postRepository.existsById(postId)) {
            postRepository.deleteById(postId);
            return true;
        }

        return false; // Post not found
    }

}
