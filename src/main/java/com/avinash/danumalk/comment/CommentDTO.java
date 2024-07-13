package com.avinash.danumalk.comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long commentId;
    private Long postId;
    private Long parentCommentId;
    private String content;
    private Date createdAt;
    private Date updatedAt;
    private String postCreatedBy;


    @JsonIgnore
    private Integer userId;
}
