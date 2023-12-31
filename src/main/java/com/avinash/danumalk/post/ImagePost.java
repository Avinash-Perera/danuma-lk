package com.avinash.danumalk.post;

import com.avinash.danumalk.post.Post;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "image-post")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImagePost extends Post {

    @Column(name = "image_data")
    private String imageUrl;

    @Column(name = "image_description", length = 1000) // Image description field
    private String imageDescription;

}
