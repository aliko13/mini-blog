package com.example.mini_blog.service;

import com.example.mini_blog.data.entity.User;
import com.example.mini_blog.data.repository.UserRepository;
import com.example.mini_blog.dto.user.UserCreateRequest;
import com.example.mini_blog.dto.user.UserResponse;
import com.example.mini_blog.dto.user.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository users;

    public Page<UserResponse> list(Pageable pageable) {
        return users.findAll(pageable).map(this::toDto);
    }

    public UserResponse create(UserCreateRequest req) {
        User u = User.builder().username(req.username()).email(req.email()).build();
        return toDto(users.save(u));
    }

    public UserResponse get(Long id) {
        return users.findById(id).map(this::toDto).orElseThrow(() -> notFound("User", id));
    }

    public UserResponse update(Long id, UserUpdateRequest req) {
        User u = users.findById(id).orElseThrow(() -> notFound("User", id));
        u.setUsername(req.username());
        u.setEmail(req.email());
        return toDto(users.save(u));
    }

    public void delete(Long id) {
        if (!users.existsById(id)) throw notFound("User", id);
        users.deleteById(id);
    }

    private RuntimeException notFound(String type, Object id) {
        return new IllegalArgumentException(type + " not found: " + id);
    }

    private UserResponse toDto(User u) {
        return new UserResponse(u.getId(), u.getUsername(), u.getEmail());
    }
}
