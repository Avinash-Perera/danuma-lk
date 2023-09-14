package com.avinash.danumalk.dto;

import com.avinash.danumalk.model.Comment;
import com.avinash.danumalk.model.PostType;
import com.avinash.danumalk.model.Reaction;
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
    private String title;
    private PostType postType;
    private Date createdAt;
    private Date updatedAt;
    private List<Comment> comments;
    private List<Reaction> reactions;
}
