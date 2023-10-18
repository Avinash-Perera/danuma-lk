package com.avinash.danumalk.reaction;

import com.avinash.danumalk.post.Post;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "reaction_type")
@Data
@NoArgsConstructor
public abstract class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reaction_id")
    private Long id;

    // Reference to the associated post
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

}
