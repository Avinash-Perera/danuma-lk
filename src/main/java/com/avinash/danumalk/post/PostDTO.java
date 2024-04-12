package com.avinash.danumalk.post;

import com.avinash.danumalk.reactions.LikeReactionResponseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {

    private Long postId;

    @NotEmpty(message = "Field cannot be empty")
    @Size(min = 1, max = 200, message = "Title length must be between 1 and 200 characters")
    private String title;

    private PostType postType;

    private Date createdAt;

    private Date updatedAt;

    private String postCreatedBy;

    @JsonIgnore
    private Integer userId;

    private List<LikeReactionResponseDTO> likes;

    private int likeCount;  // Total count of likes

    private boolean userLiked;


}
