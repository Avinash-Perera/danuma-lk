package com.avinash.danumalk.reactions;

import com.avinash.danumalk.post.Post;
import com.avinash.danumalk.user.User;

public interface Reaction {
    Post getPost();

    void setPost(Post post);

    User getUser();

    void setUser(User user);

}
