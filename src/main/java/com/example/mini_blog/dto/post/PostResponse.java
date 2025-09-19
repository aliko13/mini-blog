package com.example.mini_blog.dto.post;

import java.time.Instant;

public record PostResponse(
        Long id,
        String title,
        String content,
        Long authorId,
        String authorUsername,
        Instant createdAt,
        Instant updatedAt
) {}
