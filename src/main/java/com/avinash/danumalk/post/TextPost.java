package com.avinash.danumalk.post;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "text_post")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TextPost extends Post {
    @Column(name = "content", length = 5000)
    private String content;
}
