package com.avinash.danumalk.dto;

import com.avinash.danumalk.model.PostType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Long postId;
    private String title;
    private PostType postType;
    private Date createdAt;
    private Date updatedAt;



    public void setError(boolean b) {
    }

    public void setErrorMessage(String errorMessage) {
    }
}
