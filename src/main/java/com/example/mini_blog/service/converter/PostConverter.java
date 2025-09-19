package com.example.mini_blog.service.converter;

import com.example.mini_blog.data.entity.Post;
import com.example.mini_blog.dto.post.PostResponse;

public class PostConverter {
    public PostResponse toDto(Post p) {
        return new PostResponse(
                p.getId(), p.getTitle(), p.getContent(),
                p.getAuthor().getId(), p.getAuthor().getUsername(),
                p.getCreatedAt(), p.getUpdatedAt()
        );
    }
}
