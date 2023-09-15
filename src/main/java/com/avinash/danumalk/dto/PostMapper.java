package com.avinash.danumalk.dto;

import com.avinash.danumalk.model.ImagePost;
import com.avinash.danumalk.model.Post;
import com.avinash.danumalk.model.TextPost;
import com.avinash.danumalk.model.VideoPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {
    @Autowired
    private ImagePostMapper imagePostMapper;

    @Autowired
    private VideoPostMapper videoPostMapper;

    @Autowired
    private TextPostMapper textPostMapper;


    public PostDTO postToDTO(Post post) {
        if (post instanceof ImagePost) {
            return imagePostMapper.imagePostToDTO((ImagePost) post);
        } else if (post instanceof VideoPost) {
            return videoPostMapper.videoPostToDTO((VideoPost) post);
        } else if (post instanceof TextPost) {
            return textPostMapper.textPostToDTO((TextPost) post);
        } else {
            // Handle other post types if needed
            return new PostDTO(
                    post.getPostId(),
                    post.getTitle(),
                    post.getPostType(),
                    post.getCreatedAt(),
                    post.getUpdatedAt()
            );
        }
    }


    public Post dtoToPost(PostDTO postDTO) {
        Post post = new Post();
        post.setPostId(postDTO.getPostId());
        post.setTitle(postDTO.getTitle());
        post.setPostType(postDTO.getPostType());
        post.setCreatedAt(postDTO.getCreatedAt());
        post.setUpdatedAt(postDTO.getUpdatedAt());
        return post;
    }

}