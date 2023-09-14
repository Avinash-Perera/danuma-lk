package com.avinash.danumalk.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Table(name = "image-post")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImagePost extends Post{

    @Column(name = "image_data")
    private String imageUrl;

    @Column(name = "image_description", length = 1000) // Image description field
    private String imageDescription;

}
