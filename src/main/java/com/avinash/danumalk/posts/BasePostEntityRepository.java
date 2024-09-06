package com.avinash.danumalk.posts;



import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BasePostEntityRepository extends JpaRepository<BasePostEntity, UUID> {


}
