package com.avinash.danumalk.posts.image_post;

import com.avinash.danumalk.posts.PostCategory;
import com.avinash.danumalk.posts.PostType;
import com.avinash.danumalk.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Repository
@AllArgsConstructor
public class ImagePostRepositoryImpl implements ImagePostRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;
    private final ImagePostMapper imagePostMapper;

    private static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }

    private static long getReactionCount(EntityManager entityManager, UUID postId) {
        String countJpql = "SELECT COUNT(r) FROM Reaction r WHERE r.post.id = :postId";
        Query countQuery = entityManager.createQuery(countJpql);
        countQuery.setParameter("postId", postId);
        return (long) countQuery.getSingleResult();
    }

    private static boolean hasUserReacted(EntityManager entityManager, UUID postId, UUID userId) {
        String userReactedJpql = "SELECT COUNT(r) FROM Reaction r WHERE r.post.id = :postId AND r.owner.id = :userId";
        Query userReactedQuery = entityManager.createQuery(userReactedJpql);
        userReactedQuery.setParameter("postId", postId);
        userReactedQuery.setParameter("userId", userId);
        return (long) userReactedQuery.getSingleResult() > 0;
    }

    private static long getCommentCount(EntityManager entityManager, UUID postId) {
        String commentCountJpql = "SELECT COUNT(c) FROM Comment c WHERE c.post.id = :postId";
        Query commentCountQuery = entityManager.createQuery(commentCountJpql);
        commentCountQuery.setParameter("postId", postId);
        return (long) commentCountQuery.getSingleResult();
    }


    @Override
    @Transactional
    public ImagePost saveAndFetchWithPostType(ImagePost imagePost, UUID postTypeId,  List<UUID> postCategoryIds) {
        if (imagePost.getPostType() == null && postTypeId != null) {
            PostType postType = entityManager.find(PostType.class, postTypeId);
            imagePost.setPostType(postType);
        }
//
//        // Set categories if not already set
//        if ((imagePost.getCategories() == null || imagePost.getCategories().isEmpty()) && postCategoryIds != null) {
//            List<PostCategory> categories = entityManager.createQuery(
//                            "SELECT pc FROM PostCategory pc WHERE pc.id IN :ids", PostCategory.class)
//                    .setParameter("ids", postCategoryIds)
//                    .getResultList();
//
//            if (categories.isEmpty() && !postCategoryIds.isEmpty()) {
//                throw new IllegalArgumentException("No PostCategories found with the provided IDs");
//            }
//
//            imagePost.setCategories(categories);
//        }
        ImagePost savedImagePost = entityManager.merge(imagePost);

        // Use JPQL to fetch the ImagePost with PostType using a LEFT JOIN
        String jpql = "SELECT ip FROM ImagePost ip LEFT JOIN FETCH ip.postType WHERE ip.id = :postId";
        TypedQuery<ImagePost> query = entityManager.createQuery(jpql, ImagePost.class);
        query.setParameter("postId", savedImagePost.getId());

        return query.getSingleResult();
    }

    @Override
    @Transactional
    public ImagePostResponse getImagePostDetails(UUID postId) {
        String jpql = "SELECT p FROM ImagePost p WHERE p.id = :postId";
        TypedQuery<ImagePost> query = entityManager.createQuery(jpql, ImagePost.class);
        query.setParameter("postId", postId);
        ImagePost imagePost = query.getSingleResult();

        long reactionCount = getReactionCount(entityManager, postId);  // Pass entityManager
        long commentCount = getCommentCount(entityManager, postId);
        User currentUser = getCurrentUser();  // Still an instance method

        Boolean isUserReacted = currentUser != null ? hasUserReacted(entityManager, postId, currentUser.getId()) : null;

        return imagePostMapper.mapToImagePostResponse(imagePost, reactionCount, commentCount, isUserReacted);
    }


//Jpa default handles missing categories in the request list to delete them from the db
    @Transactional
    @Override
    public ImagePostResponse updateImagePost(ImagePost imagePost, List<UUID> postCategoryIds) {
        if (imagePost == null) {
            throw new IllegalArgumentException("Post not found");
        }

        // Fetch the current categories associated with the image post, initialize to an empty list if null
        List<PostCategory> currentCategories = imagePost.getCategories();
        if (currentCategories == null) {
            currentCategories = new ArrayList<>();
        }

        // Fetch categories by their IDs from the request
        List<PostCategory> requestedCategories = entityManager.createQuery(
                        "SELECT pc FROM PostCategory pc WHERE pc.id IN :ids", PostCategory.class)
                .setParameter("ids", postCategoryIds)
                .getResultList();

        if (requestedCategories.isEmpty() && !postCategoryIds.isEmpty()) {
            throw new IllegalArgumentException("No PostCategories found with the provided IDs");
        }



        // Add new categories from the requested list, if not already present
        for (PostCategory category : requestedCategories) {
            if (!currentCategories.contains(category)) {
                currentCategories.add(category);
            }
        }

        // Set the updated list of categories
        imagePost.setCategories(currentCategories);

        // Update the post in the database
        ImagePost updatedPost = entityManager.merge(imagePost);
        UUID postId = updatedPost.getId();

        return getImagePostDetails(postId);
    }



    @Override
    @Transactional
    public Page<ImagePostResponse> getPaginatedImagePostsWithReactions(int page, int size, UUID userId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());  // Change to descending or ascending based on requirement

        // Base JPQL query to fetch ImagePosts with reactions
        String jpql = "SELECT DISTINCT ip FROM ImagePost ip LEFT JOIN FETCH ip.reactions r";

        // Append ORDER BY clause based on pageable's sorting information
        Sort sort = pageable.getSort();
        if (sort.isSorted()) {
            jpql += " ORDER BY " + sort.stream()
                    .map(order -> "ip." + order.getProperty() + " " + order.getDirection().name())
                    .collect(Collectors.joining(", "));
        }

        // Create the query with sorting
        TypedQuery<ImagePost> query = entityManager.createQuery(jpql, ImagePost.class);
        query.setFirstResult(page * size);
        query.setMaxResults(size);

        // Execute query to get posts
        List<ImagePost> posts = query.getResultList();

        // Map ImagePost entities to ImagePostResponse DTOs
        List<ImagePostResponse> responses = posts.stream()
                .map(imagePost -> {
                    long reactionCount = getReactionCount(entityManager, imagePost.getId());  // Get reaction count
                    long commentCount = getCommentCount(entityManager, imagePost.getId());    // Get comment count
                    Boolean isUserReacted = userId != null ? hasUserReacted(entityManager, imagePost.getId(), userId) : null;
//                    boolean isUserReacted = hasUserReacted(entityManager, imagePost.getId(), userId);  // Check if user reacted
                    return imagePostMapper.mapToImagePostResponse(imagePost, reactionCount, commentCount, isUserReacted);
                })
                .toList();

        // Query for total number of posts (for pagination metadata)
        String countTotalJpql = "SELECT COUNT(ip) FROM ImagePost ip";
        Query totalQuery = entityManager.createQuery(countTotalJpql);
        long totalElements = (long) totalQuery.getSingleResult();

        // Return the paginated response
        return new PageImpl<>(responses, pageable, totalElements);
    }


}
