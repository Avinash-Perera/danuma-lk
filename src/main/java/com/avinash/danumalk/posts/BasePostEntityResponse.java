package com.avinash.danumalk.posts;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
public class BasePostEntityResponse {
    private UUID id;
    private PostTypeResponse postTypeResponse;
    private String postOwner;
}
