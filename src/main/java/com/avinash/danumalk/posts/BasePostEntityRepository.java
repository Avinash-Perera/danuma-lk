package com.avinash.danumalk.posts;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface BasePostEntityRepository extends JpaRepository<BasePostEntity, UUID>, PostRepositoryCustom{
    @Query("SELECT b FROM BasePostEntity b " +
            "LEFT JOIN ImagePost i ON b.id = i.id AND b.postType = :imagePostType " +
            "ORDER BY b.createdDate DESC")
    Page<BasePostEntity> findAllPostsWithJoins(@Param("imagePostType") PostType imagePostType,
                                               Pageable pageable);

}



