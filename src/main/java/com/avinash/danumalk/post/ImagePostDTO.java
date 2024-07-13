package com.avinash.danumalk.post;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImagePostDTO extends PostDTO {
    private List<String> imageUrls;       // Store the full URLs if needed



    @NotNull(message = "Image Description cannot be null")
    @Size(min = 1, max = 5000, message = "Title length must be between 1 and 5000 characters")
    private String imageDescription;



}
