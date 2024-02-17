package com.avinash.danumalk.post;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PostMapper {
    private final ImagePostMapper imagePostMapper;
    private final TextPostMapper textPostMapper;


    /**
     * Converts a Post object to a PostDTO object.
     *
     * @param  post   the Post object to convert
     * @return        the converted PostDTO object
     */
    public PostDTO postToDTO(Post post) {
        if (post instanceof ImagePost) {
            return imagePostMapper.imagePostToDTO((ImagePost) post);
        } else {
            return textPostMapper.textPostToDTO((TextPost) post);

        }
    }


    /**
     * Converts a PostDTO object to a Post object.
     *
     * @param  postDTO   the PostDTO object to be converted
     * @return           the converted Post object
     */

    public Post dtoToPost(PostDTO postDTO) {
        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setPostType(postDTO.getPostType());
        post.setCreatedAt(postDTO.getCreatedAt());
        post.setUpdatedAt(postDTO.getUpdatedAt());
        return post;
    }

}