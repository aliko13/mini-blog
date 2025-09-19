package com.example.mini_blog.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PostCreateRequest(
        @NotBlank @Size(max=140) String title,
        @NotBlank @Size(max=4000) String content,
        @NotNull Long authorId
) {}
