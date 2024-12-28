package com.avinash.danumalk.posts.image_post;

public class ImagePostWithReactionCount {
    private ImagePost imagePost;
    private long reactionCount;

    public ImagePostWithReactionCount(ImagePost imagePost, long reactionCount) {
        this.imagePost = imagePost;
        this.reactionCount = reactionCount;
    }

    public ImagePost getImagePost() {
        return imagePost;
    }

    public long getReactionCount() {
        return reactionCount;
    }
}
