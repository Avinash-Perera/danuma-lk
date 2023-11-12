package com.avinash.danumalk.config;

import com.avinash.danumalk.post.*;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Define a TypeMap to handle the mapping for VideoPost
        modelMapper.createTypeMap(VideoPost.class, VideoPostDTO.class)
                .addMapping(VideoPost::getVideoUrl, VideoPostDTO::setVideoUrl)
                .addMapping(VideoPost::getVideoDescription, VideoPostDTO::setVideoDescription);

        // Define a TypeMap to handle the mapping for ImagePost
        modelMapper.createTypeMap(ImagePost.class, ImagePostDTO.class)
                .addMapping(ImagePost::getImageUrl, ImagePostDTO::setImageUrl)
                .addMapping(ImagePost::getImageDescription, ImagePostDTO::setImageDescription);

        // Define a TypeMap to handle the mapping for TextPost
        modelMapper.createTypeMap(TextPost.class, TextPostDTO.class)
                .addMapping(TextPost::getContent, TextPostDTO::setContent);


        // You can add more mappings for other entities and DTOs here

        return modelMapper;
    }
}

