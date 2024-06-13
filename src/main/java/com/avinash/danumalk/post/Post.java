package com.avinash.danumalk.post;

import com.avinash.danumalk.comment.Comment;
import com.avinash.danumalk.reactions.LikeReaction;
import com.avinash.danumalk.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "post")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    public Post(Long postId) {
        this.postId = postId;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @Column(name = "title", length = 200)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_type")
    private PostType postType;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt; // Use Timestamp

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt; // Use Timestamp

    // Add the ManyToOne relationship for the user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties("posts")  // Use this annotation to prevent infinite loop during JSON serialization
    private User user;

    // Define a one-to-many relationship with comments
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("post") // Use this annotation
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("post") // Use this annotation
    private List<LikeReaction> likeReactions;




}
