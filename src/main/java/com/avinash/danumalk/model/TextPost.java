package com.avinash.danumalk.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "text_post")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TextPost extends Post{
    @Column(name = "content", length = 1000)
    private String content;
}
