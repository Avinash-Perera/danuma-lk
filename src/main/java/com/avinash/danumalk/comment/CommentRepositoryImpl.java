package com.avinash.danumalk.comment;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class CommentRepositoryImpl implements CusCommentRepository {

    private final EntityManager entityManager;
    private final CommentMapper commentMapper;


    @Override
    @Transactional
    public CommentResponse saveAndMapToResponse(Comment comment, Authentication connectedUser) {
        // Custom logic before saving
        if (comment.getId() == null) {
            // New comment, use persist
            entityManager.persist(comment);
        } else {
            // Existing comment, use merge
            entityManager.merge(comment);
        }

        // Custom logic after saving
        return commentMapper.mapToCommentResponse(comment, connectedUser);
    }
}
