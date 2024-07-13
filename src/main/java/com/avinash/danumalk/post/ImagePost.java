package com.avinash.danumalk.post;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "image-post")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImagePost extends Post {

    @ElementCollection
    @CollectionTable(name = "image_post_urls", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "image_url")
    private List<String> imageUrls;

    @Column(name = "image_description", length = 1000) // Image description field
    private String imageDescription;

}
