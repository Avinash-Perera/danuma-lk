package com.avinash.danumalk.posts;

import com.avinash.danumalk.posts.image_post.ImagePost;
import com.avinash.danumalk.posts.image_post.ImagePostMapper;
import com.avinash.danumalk.posts.image_post.ImagePostResponse;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.EntityManager;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom{

    @PersistenceContext
    private final EntityManager entityManager;

    private final ImagePostMapper imagePostMapper;

    @Override
    @Transactional
    public Page<PostsResponse> getAllPostsSortedByDate(Pageable pageable, UUID userId) {
        String jpql = "SELECT b FROM BasePostEntity b LEFT JOIN b.postType pt ORDER BY b.createdDate DESC";

        // Create and execute the query
        TypedQuery<BasePostEntity> query = entityManager.createQuery(jpql, BasePostEntity.class);
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        List<BasePostEntity> posts = query.getResultList();

        List<PostsResponse> postResponses = new ArrayList<>();

        for (BasePostEntity post : posts) {
            if (post instanceof ImagePost imagePost) {
                long reactionCount = getReactionCount(imagePost.getId());
                long commentCount = getCommentCount(imagePost.getId());
                Boolean isUserReacted = userId != null ? hasUserReacted(imagePost.getId(), userId) : null;
//                boolean isUserReacted = hasUserReacted(imagePost.getId(), userId);

                ImagePostResponse imageResponse = imagePostMapper.mapToImagePostResponse(imagePost, reactionCount, commentCount, isUserReacted);

                PostsResponse postsResponse = PostsResponse.builder()
                        .imagePostResponse(imageResponse)
                        .build();

                postResponses.add(postsResponse);
            }
            // You can add else-if here for other post types (e.g., TextPost)
        }

        // Query for total number of posts for pagination metadata
        long totalElements = countTotalPosts();

        // Use pageable.getPageNumber() and pageable.getPageSize() to create PageImpl
        return new PageImpl<>(postResponses, pageable, totalElements); // Pass the pageable directly
    }

    @Override
    @Transactional
    public Page<PostsResponse> getAllPostsByUserIdSortedByDate(Pageable pageable, UUID userId) {
        // Ensure userId is provided
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        String jpql = "SELECT b FROM BasePostEntity b WHERE b.owner.id = :userId ORDER BY b.createdDate DESC";

        // Create and execute the query
        TypedQuery<BasePostEntity> query = entityManager.createQuery(jpql, BasePostEntity.class);
        query.setParameter("userId", userId);
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        List<BasePostEntity> posts = query.getResultList();

        List<PostsResponse> postResponses = new ArrayList<>();

        for (BasePostEntity post : posts) {
            if (post instanceof ImagePost imagePost) {
                long reactionCount = getReactionCount(imagePost.getId());
                long commentCount = getCommentCount(imagePost.getId());
                Boolean isUserReacted = hasUserReacted(imagePost.getId(), userId);

                ImagePostResponse imageResponse = imagePostMapper.mapToImagePostResponse(
                        imagePost, reactionCount, commentCount, isUserReacted
                );

                PostsResponse postsResponse = PostsResponse.builder()
                        .imagePostResponse(imageResponse)
                        .build();

                postResponses.add(postsResponse);
            }
        }

        // Query for total number of posts by user for pagination metadata
        long totalElements = countPostsByUser(userId);

        // Use pageable.getPageNumber() and pageable.getPageSize() to create PageImpl
        return new PageImpl<>(postResponses, pageable, totalElements);
    }

    private long countPostsByUser(UUID userId) {
        String countTotalJpql = "SELECT COUNT(b) FROM BasePostEntity b WHERE b.owner.id = :userId";
        return (long) entityManager.createQuery(countTotalJpql)
                .setParameter("userId", userId)
                .getSingleResult();
    }

    private long getReactionCount(UUID postId) {
        String countJpql = "SELECT COUNT(r) FROM Reaction r WHERE r.post.id = :postId";
        return (long) entityManager.createQuery(countJpql)
                .setParameter("postId", postId)
                .getSingleResult();
    }

    private long getCommentCount(UUID postId) {
        String commentCountJpql = "SELECT COUNT(c) FROM Comment c WHERE c.post.id = :postId";
        return (long) entityManager.createQuery(commentCountJpql)
                .setParameter("postId", postId)
                .getSingleResult();
    }

    private boolean hasUserReacted(UUID postId, UUID userId) {
        String userReactedJpql = "SELECT COUNT(r) FROM Reaction r WHERE r.post.id = :postId AND r.owner.id = :userId";
        return (long) entityManager.createQuery(userReactedJpql)
                .setParameter("postId", postId)
                .setParameter("userId", userId)
                .getSingleResult() > 0;
    }

    private long countTotalPosts() {
        String countTotalJpql = "SELECT COUNT(b) FROM BasePostEntity b";
        return (long) entityManager.createQuery(countTotalJpql).getSingleResult();
    }
}
