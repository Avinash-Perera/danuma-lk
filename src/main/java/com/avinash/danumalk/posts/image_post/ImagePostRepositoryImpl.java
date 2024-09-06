package com.avinash.danumalk.posts.image_post;

import com.avinash.danumalk.posts.PostType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Slf4j
@Repository
public class ImagePostRepositoryImpl implements ImagePostRepositoryCustom{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public ImagePost saveAndFetchWithPostType(ImagePost imagePost, UUID postTypeId) {
        // Save the ImagePost entity
        if (imagePost.getPostType() == null && postTypeId != null) {
            PostType postType = entityManager.find(PostType.class, postTypeId);
            imagePost.setPostType(postType);
        }
        ImagePost savedImagePost = entityManager.merge(imagePost);

        // Use Criteria API to perform a LEFT JOIN and fetch the ImagePost with PostType
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ImagePost> criteriaQuery = criteriaBuilder.createQuery(ImagePost.class);
        Root<ImagePost> imagePostRoot = criteriaQuery.from(ImagePost.class);
        Join<ImagePost, PostType> postTypeJoin = imagePostRoot.join("postType", JoinType.LEFT);

        criteriaQuery.select(imagePostRoot)
                .where(criteriaBuilder.equal(imagePostRoot.get("id"), savedImagePost.getId()));

        ImagePost resultImagePost = entityManager.createQuery(criteriaQuery).getSingleResult();

        // Log the result
        log.info("Fetched ImagePost with PostType: {}", resultImagePost);

        return resultImagePost;
    }
}
