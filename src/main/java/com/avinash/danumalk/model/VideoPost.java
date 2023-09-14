package com.avinash.danumalk.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "video-post")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoPost extends Post{

    @Column(name = "video_data")
    private String videoUrl;

    @Column(name = "video_description", length = 1000) // Video description field
    private String videoDescription;
}
