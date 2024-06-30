package com.avinash.danumalk.user;

import com.avinash.danumalk.profileImage.ProfileImage;
import com.avinash.danumalk.role.Role;
import com.avinash.danumalk.token.Token;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "_user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String usersName;


    @Column(unique = true)
    private String email;

    private String password;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @ToString.Exclude
    private List<Role> roles;

//    @CreatedDate
//    @Column(nullable = false, updatable = false)
//    private LocalDateTime createdDate;
//
//    @LastModifiedDate
//    @Column(insertable = false)
//    private LocalDateTime lastModifiedDate;

    private String profile_image_url;

    private boolean accountLocked;

    private boolean enabled;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private List<Token> tokens;


    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_image_id")
    @ToString.Exclude
    private ProfileImage profileImage;

//    // Add the OneToMany relationship for posts
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    @JsonIgnoreProperties("user")  // Use this annotation to prevent infinite loop during JSON serialization
//    @ToString.Exclude
//    private List<Post> posts;
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    @JsonIgnoreProperties("user")
//    @ToString.Exclude
//    private List<Comment> comments;
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    @JsonIgnoreProperties("user")
//    @ToString.Exclude
//    private List<LikeReaction> likeReactions;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .flatMap(role -> role.getAuthorities().stream())
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    @Override
    public boolean isEnabled() {
        return enabled;
    }
}

