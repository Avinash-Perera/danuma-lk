//package com.avinash.danumalk.user;
//
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//import static com.avinash.danumalk.user.Permission.*;
//
//@Getter
//@RequiredArgsConstructor
//public enum Role {
//
//    USER(
//            Set.of(
//                    USER_READ, USER_UPDATE, USER_DELETE, USER_CREATE, POST_READ, POST_CREATE, POST_UPDATE, POST_DELETE,
//                    COMMENT_READ, COMMENT_CREATE, COMMENT_UPDATE, COMMENT_DELETE, REACTION_READ, REACTION_CREATE, REACTION_UPDATE,
//                    REACTION_DELETE
//    )),
//    ADMIN(
//            Set.of(
//                    ADMIN_READ, ADMIN_UPDATE, ADMIN_DELETE, ADMIN_CREATE, USER_READ, USER_UPDATE, USER_DELETE, USER_CREATE,
//                    POST_READ, POST_CREATE, POST_UPDATE, POST_DELETE, COMMENT_READ, COMMENT_CREATE, COMMENT_UPDATE, COMMENT_DELETE,
//                    REACTION_READ, REACTION_CREATE, REACTION_UPDATE, REACTION_DELETE
//
//            )
//    ),
//
//    ;
//    private final Set<Permission> permissions;
//
//    /**
//     * Retrieves the authorities of the user.
//     *
//     * @return         	a list of SimpleGrantedAuthority objects representing the user's authorities
//     */
//    public List<SimpleGrantedAuthority> getAuthorities() {
//        var authorities = getPermissions()
//                .stream()
//                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
//                .collect(Collectors.toList());
//        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
//        return authorities;
//    }
//}
