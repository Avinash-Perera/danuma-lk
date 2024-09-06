package com.avinash.danumalk.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class PostTypeResponse {
    private UUID id;
    private String type_name;
    private String type_case_name;


}
