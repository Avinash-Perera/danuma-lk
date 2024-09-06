package com.avinash.danumalk.posts;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class PostType {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String type_name;

    private String type_case_name;
}
