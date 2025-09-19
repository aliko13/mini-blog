package com.example.mini_blog.controller;

import com.example.mini_blog.dto.user.UserCreateRequest;
import com.example.mini_blog.dto.user.UserResponse;
import com.example.mini_blog.dto.user.UserUpdateRequest;
import com.example.mini_blog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    public Page<UserResponse> list(Pageable pageable) {
        return service.list(pageable);
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(
            @Valid @RequestBody UserCreateRequest req,
            UriComponentsBuilder uri
    ) {
        UserResponse created = service.create(req);
        return ResponseEntity.created(
                uri.path("/api/users/{id}").buildAndExpand(created.id()).toUri()
        ).body(created);
    }

    @GetMapping("/{id}")
    public UserResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    public UserResponse update(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
