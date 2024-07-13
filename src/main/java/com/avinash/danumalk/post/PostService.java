package com.avinash.danumalk.post;

import com.avinash.danumalk.common.PageResponse;
import com.avinash.danumalk.util.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService implements PostServiceInterface {
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final SecurityUtils securityUtils;



    @Override
    public PageResponse<PostDTO> getAllPostsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Post> postPage = postRepository.findAll(pageable);
        List<PostDTO> postDTOs = postPage.stream()
                .map(post -> postMapper.postToDTO(post, securityUtils.getAuthenticatedUserId()))
                .collect(Collectors.toList());

        return new PageResponse<>(
                postDTOs,
                postPage.getNumber(),
                postPage.getSize(),
                postPage.getTotalElements(),
                postPage.getTotalPages(),
                postPage.isFirst(),
                postPage.isLast()
        );
    }


    @Override
    public PostDTO getPostById(Long postId) {
        Integer authenticatedUserId = securityUtils.getAuthenticatedUserId();
        var post = postRepository.findById(postId).orElse(null);

        if (post != null) {
            return postMapper.postToDTO(post, securityUtils.getAuthenticatedUserId()); // Convert Post to PostDTO using the mapper
        }
        return null;
    }


    @Override
    public Optional<Post> findById(Long postId) {
        return postRepository.findById(postId);
    }
}