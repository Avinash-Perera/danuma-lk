package com.avinash.danumalk.posts.image_post;

import com.avinash.danumalk.posts.BasePostEntity;
import com.avinash.danumalk.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@SuperBuilder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class ImagePost extends BasePostEntity {

    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    @OrderColumn
    private List<String> imageUrls;

}
