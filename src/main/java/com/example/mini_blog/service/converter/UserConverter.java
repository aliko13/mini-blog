package com.example.mini_blog.service.converter;

import com.example.mini_blog.data.entity.User;
import com.example.mini_blog.dto.user.UserCreateRequest;
import com.example.mini_blog.dto.user.UserResponse;

public class UserConverter {
    public User toEntity(UserCreateRequest userCreateRequest) {
        return User.builder()
                .username(userCreateRequest.username())
                .email(userCreateRequest.email())
                .build();
    }

    public UserResponse toDto(User u) {
        return new UserResponse(u.getId(), u.getUsername(), u.getEmail());
    }
}
