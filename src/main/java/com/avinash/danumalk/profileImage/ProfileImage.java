package com.avinash.danumalk.profileImage;

import com.avinash.danumalk.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//Profile image 1.0

@Entity
@Table(name = "profile_img")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    @Lob
    @Column(name = "imagedata",length = 1000)
    private byte[] imageData;

    @OneToOne
    @JoinColumn(name = "user_id",unique = true)
    private User user;

}
