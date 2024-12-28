//package com.avinash.danumalk.oldfiles;
//
//import com.avinash.danumalk.comment.Comment;
//import com.avinash.danumalk.posts.BasePostEntity;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.persistence.criteria.*;
//import org.springframework.stereotype.Repository;
//
//import java.util.UUID;
//
//@Repository
//public class CustomCommentRepositoryImpl implements CustomCommentRepository {
//
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    @Override
//    public Comment createCommentWithPost(UUID postId, Comment comment) {
//        // Create CriteriaBuilder
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//
//        // Create CriteriaQuery for the Comment entity
//        CriteriaQuery<Comment> cq = cb.createQuery(Comment.class);
//        Root<Comment> commentRoot = cq.from(Comment.class);
//
//        // Perform a left join between Comment and BasePostEntity
//        Join<Comment, BasePostEntity> postJoin = commentRoot.join("post", JoinType.LEFT);
//
//        // Create a query to select comments with the related post (if any)
//        cq.select(commentRoot)
//                .where(cb.or(
//                        cb.equal(postJoin.get("id"), postId),
//                        cb.isNull(postJoin.get("id"))
//                ));
//
//        // Create and execute the query
//        Comment existingComment = entityManager.createQuery(cq).getSingleResult();
//
//        // Set post and persist comment
//        if (existingComment.getPost() != null) {
//            comment.setPost(existingComment.getPost());
//        }
//        entityManager.persist(comment);
//
//        return comment;
//    }
//}
