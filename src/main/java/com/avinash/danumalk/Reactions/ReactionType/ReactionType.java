package com.avinash.danumalk.Reactions.ReactionType;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "reaction_types")
@Data
@NoArgsConstructor
public class ReactionType {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID reaction_type_id;

    @Column(nullable = false, unique = true)
    private String name;  // E.g., "Like", "Dislike"

    @Column(nullable = false, unique = true)
    private String key;  // E.g., "LIKE", "DISLIKE"

}
