package com.avinash.danumalk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImagePostDTO extends PostDTO {
    private String imageUrl;
    private String imageDescription;
}
