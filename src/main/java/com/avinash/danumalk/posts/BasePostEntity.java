package com.avinash.danumalk.posts;

import com.avinash.danumalk.Reactions.Reaction.Reaction;
import com.avinash.danumalk.comment.Comment;
import com.avinash.danumalk.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "posts")
@Inheritance(strategy = InheritanceType.JOINED)
public class BasePostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_type_id")
    private PostType postType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    @JsonIgnoreProperties("posts")  // Use this annotation to prevent infinite loop during JSON serialization
    private User owner;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Reaction> reactions;

    @ManyToMany
    @JoinTable(
            name = "post_category_mapping",  // Join table name
            joinColumns = @JoinColumn(name = "post_id"),  // Foreign key for Post
            inverseJoinColumns = @JoinColumn(name = "category_id") // Foreign key for Category
    )
    @ToString.Exclude
    private List<PostCategory> categories = new ArrayList<>();

}
