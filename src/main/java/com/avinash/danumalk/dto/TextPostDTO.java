package com.avinash.danumalk.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TextPostDTO extends PostDTO{

    @NotNull(message = "Content cannot be null")
    @Size(min = 1, max = 5000, message = "Title length must be between 1 and 5000 characters")
    private String content;
}
