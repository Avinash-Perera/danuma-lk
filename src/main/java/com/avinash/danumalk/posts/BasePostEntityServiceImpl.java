package com.avinash.danumalk.posts;

import com.avinash.danumalk.user.User;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class BasePostEntityServiceImpl implements BasePostEntityService {

    private final BasePostEntityRepository basePostEntityRepository;



    @Override
    public Page<PostsResponse> getAllPostsSortedByDate(Pageable pageable,  @Nullable Authentication connectedUser) {
        UUID userId = null;
        if (connectedUser != null) {
            User user = (User) connectedUser.getPrincipal();
            userId = user.getId();
        }

        return basePostEntityRepository.getAllPostsSortedByDate(pageable, userId);
    }

}
