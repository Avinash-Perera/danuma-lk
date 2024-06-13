package com.avinash.danumalk.role;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),

    USER_READ("user:read"),
    USER_UPDATE("user:update"),
    USER_CREATE("user:create"),
    USER_DELETE("user:delete"),

    POST_READ("post:read"),
    POST_UPDATE("post:update"),
    POST_CREATE("post:create"),
    POST_DELETE("post:delete"),

    COMMENT_READ("comment:read"),
    COMMENT_UPDATE("comment:update"),
    COMMENT_CREATE("comment:create"),
    COMMENT_DELETE("comment:delete"),

    REACTION_READ("reaction:read"),
    REACTION_UPDATE("reaction:update"),
    REACTION_CREATE("reaction:create"),
    REACTION_DELETE("reaction:delete")

    ;

    private final String permission;
}

