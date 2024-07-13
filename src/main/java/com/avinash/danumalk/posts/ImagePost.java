package com.avinash.danumalk.posts;

import com.avinash.danumalk.common.BaseEntity;
import com.avinash.danumalk.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@Entity
@AllArgsConstructor
@NoArgsConstructor

public class ImagePost extends BaseEntity {
    private String title;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    @JsonIgnoreProperties("imagePosts")  // Use this annotation to prevent infinite loop during JSON serialization
    private User owner;

    @ElementCollection(fetch = FetchType.EAGER)
    @OrderColumn
    private List<String> imageUrls;

}
