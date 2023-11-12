package com.avinash.danumalk.post;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PostMapper {
    private final ImagePostMapper imagePostMapper;
    private final VideoPostMapper videoPostMapper;
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
        } else if (post instanceof VideoPost) {
            return videoPostMapper.videoPostToDTO((VideoPost) post);
        } else if (post instanceof TextPost) {
            return textPostMapper.textPostToDTO((TextPost) post);
        } else {
            // Handle other post types if needed
            return createErrorPostDTO();

        }
    }

    /**
     * Creates a new PostDTO object representing an error.
     *
     * @return          The newly created error PostDTO object.
     */
    private PostDTO createErrorPostDTO() {
        PostDTO errorDTO = new PostDTO();
        errorDTO.setError(true);
        errorDTO.setErrorMessage("Unsupported post type");
        return errorDTO;
    }

    /**
     * Converts a PostDTO object to a Post object.
     *
     * @param  postDTO   the PostDTO object to be converted
     * @return           the converted Post object
     */

    public Post dtoToPost(PostDTO postDTO) {
        Post post = new Post();
        post.setPostId(postDTO.getPostId());
        post.setTitle(postDTO.getTitle());
        post.setPostType(postDTO.getPostType());
        post.setCreatedAt(postDTO.getCreatedAt());
        post.setUpdatedAt(postDTO.getUpdatedAt());
        return post;
    }

}