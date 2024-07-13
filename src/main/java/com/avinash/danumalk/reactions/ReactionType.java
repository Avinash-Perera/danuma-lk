package com.avinash.danumalk.reactions;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;


@Entity
@Table(name = "reaction_types")
public class ReactionType {

    @Id
    @GeneratedValue(generator = "uuid2")    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "reaction_type_id")
    private UUID reactionTypeId;

    @Getter
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Getter
    @Column(name = "key", unique = true)
    private String key;



    public ReactionType() {
    }

    public ReactionType(String name, String key) {
        this.name = name;
        this.key = key;
    }

    public UUID getReactionTypeId() {
        return reactionTypeId;
    }

    public void setReactionTypeId(UUID reactionTypeId) {
        this.reactionTypeId = reactionTypeId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setKey(String key) {
        this.key = key;
    }
    @Override
    public String toString() {
        return "ReactionType{" +
                "reactionTypeId=" + reactionTypeId +
                ", name='" + name + '\'' +
                '}';
    }
}
