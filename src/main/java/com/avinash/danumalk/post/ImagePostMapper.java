package com.avinash.danumalk.post;
import com.avinash.danumalk.file.FileUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.List;

@Component
public class ImagePostMapper {

    private final ModelMapper modelMapper;
    @Value("${application.file.uploads.base-path}")
    private String basePath;

    @Value("${application.file.uploads.temp-file-path}")
    private String tempFilePath;

    @Value("${application.env.urls.app-url}")
    private String appUrl;

    @Autowired
    public ImagePostMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    public ImagePostDTO imagePostToDTO(ImagePost imagePost) {
        ImagePostDTO imagePostDTO = modelMapper.map(imagePost, ImagePostDTO.class);
        setImagePostCreatedBy(imagePost, imagePostDTO);
        setFullImageUrls(imagePost, imagePostDTO);
        return imagePostDTO;
    }

    private void setImagePostCreatedBy(ImagePost imagePost, ImagePostDTO imagePostDTO) {
        imagePostDTO.setPostCreatedBy(imagePost.getUser() != null ? imagePost.getUser().getUsername() : null);
    }

    private void setFullImageUrls(ImagePost imagePost, ImagePostDTO imagePostDTO) {
        if (imagePost.getImageUrls() != null && !imagePost.getImageUrls().isEmpty()) {
            List<String> fullUrls = imagePost.getImageUrls().stream()
                    .map(imageUrl -> {
                        String fullPath = basePath + tempFilePath + imageUrl;
                        if (FileUtils.readFileFromLocation(fullPath) != null) {
                            return appUrl + basePath + tempFilePath + imageUrl;
                        } else {
                            // Handle the case where the file does not exist
                            return null;
                        }
                    })
                    .filter(Objects::nonNull) // Filter out null values
                    .collect(Collectors.toList());

            List<String> fileNames = imagePost.getImageUrls().stream()
                    .map(FileUtils::extractFileName)
                    .toList();

            imagePostDTO.setImageUrls(fullUrls);

        }
    }

    public ImagePost dtoToImagePost(ImagePostDTO imagePostDTO) {
        return modelMapper.map(imagePostDTO, ImagePost.class);
    }
}


//import com.avinash.danumalk.post.ImagePost;
//import com.avinash.danumalk.post.ImagePostDTO;
//import org.springframework.stereotype.Component;
//
//@Component
//public class ImagePostMapper {
//
//    /**
//     * Converts an ImagePost object to an ImagePostDTO object.
//     *
//     * @param  imagePost  the ImagePost object to be converted
//     * @return            the converted ImagePostDTO object
//     */
//    public ImagePostDTO imagePostToDTO(ImagePost imagePost) {
//        ImagePostDTO imagePostDTO = mapToDTO(imagePost);
//        imagePostDTO.setImageUrl(imagePost.getImageUrl());
//        imagePostDTO.setImageDescription(imagePost.getImageDescription());
//        return imagePostDTO;
//    }
//
//
//    /**
//     * Converts an ImagePostDTO object to an ImagePost object.
//     *
//     * @param  imagePostDTO  the ImagePostDTO object to be converted
//     * @return               the converted ImagePost object
//     */
//    public ImagePost dtoToImagePost(ImagePostDTO imagePostDTO) {
//        ImagePost imagePost = mapToImagePost(imagePostDTO);
//        imagePost.setImageUrl(imagePostDTO.getImageUrl());
//        imagePost.setImageDescription(imagePostDTO.getImageDescription());
//        return imagePost;
//    }
//
//    //helper methods
//    private ImagePostDTO mapToDTO(ImagePost imagePost) {
//        ImagePostDTO imagePostDTO = new ImagePostDTO();
//        imagePostDTO.setPostId(imagePost.getPostId());
//        imagePostDTO.setTitle(imagePost.getTitle());
//        imagePostDTO.setPostType(imagePost.getPostType());
//        imagePostDTO.setCreatedAt(imagePost.getCreatedAt());
//        imagePostDTO.setUpdatedAt(imagePost.getUpdatedAt());
//
//        return imagePostDTO;
//    }
//
//    private ImagePost mapToImagePost(ImagePostDTO imagePostDTO) {
//        ImagePost imagePost = new ImagePost();
//        imagePost.setPostId(imagePostDTO.getPostId());
//        imagePost.setTitle(imagePostDTO.getTitle());
//        imagePost.setPostType(imagePostDTO.getPostType());
//        imagePost.setCreatedAt(imagePostDTO.getCreatedAt());
//        imagePost.setUpdatedAt(imagePostDTO.getUpdatedAt());
//        return imagePost;
//    }
//}
