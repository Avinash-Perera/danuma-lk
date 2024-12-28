package com.avinash.danumalk.posts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class PostCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String categoryName;

    private String key;

    @ManyToMany(mappedBy = "categories")
    @JsonIgnore // Avoid infinite recursion during serialization
    private List<BasePostEntity> posts = new ArrayList<>();
}
