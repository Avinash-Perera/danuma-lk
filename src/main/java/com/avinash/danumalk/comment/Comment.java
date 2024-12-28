package com.avinash.danumalk.comment;

import com.avinash.danumalk.posts.BasePostEntity;
import com.avinash.danumalk.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "comment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("comments") // Use this annotation to prevent infinite loop during JSON serialization
    @JoinColumn(name = "base_post_entity_id", referencedColumnName = "id")
    private BasePostEntity post;

    @ManyToOne
    @JsonIgnoreProperties("replies") // Use this annotation to prevent infinite loop during JSON serialization
    @JoinColumn(name = "parent_comment_id", referencedColumnName = "id")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> replies ;

    @Column(name = "content", length = 1000)
    private String content;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("comments")  // Use this annotation to prevent infinite loop during JSON serialization
    @JoinColumn(name = "owner_id")
    private User owner;


}
