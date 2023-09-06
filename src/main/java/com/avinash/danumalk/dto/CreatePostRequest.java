package com.avinash.danumalk.dto;

import com.avinash.danumalk.model.Post;
import com.avinash.danumalk.model.PostType;

public class CreatePostRequest {
    private PostType postType;
    private Post post;

    public PostType getPostType() {
        return postType;
    }

    public void setPostType(PostType postType) {
        this.postType = postType;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
