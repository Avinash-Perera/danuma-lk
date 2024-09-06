package com.avinash.danumalk.posts.image_post;

import java.util.UUID;

public interface ImagePostRepositoryCustom {
    ImagePost saveAndFetchWithPostType(ImagePost imagePost, UUID postTypeId);
}
