package com.avinash.danumalk.reactions;

import com.avinash.danumalk.post.Post;
import com.avinash.danumalk.user.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "likes")
public class LikeReaction implements Reaction{
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long likeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reaction_type_id") // Column that refers to ReactionType
    private ReactionType reactionType;
    public void setLikeId(Long likeId) {
        this.likeId = likeId;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }


    @Override
    public Post getPost() {
        return post;
    }

    @Override
    public void setPost(Post post) {
        this.post = post;
    }

    public void setReactionType(ReactionType reactionType) {
        this.reactionType = reactionType;
    }
}
