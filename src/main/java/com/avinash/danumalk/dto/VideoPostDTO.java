package com.avinash.danumalk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoPostDTO extends PostDTO {
    private String videoUrl;
    private String videoDescription;
}
