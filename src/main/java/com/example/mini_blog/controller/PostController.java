package com.example.mini_blog.controller;

import com.example.mini_blog.data.repository.PostRepository;
import com.example.mini_blog.dto.post.PostCreateRequest;
import com.example.mini_blog.dto.post.PostResponse;
import com.example.mini_blog.dto.post.PostUpdateRequest;
import com.example.mini_blog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService service;

    @GetMapping
    public Page<PostResponse> list(Pageable pageable) {
        return service.list(pageable);
    }

    @GetMapping(params = "author")
    public Page<PostResponse> listByAuthor(@RequestParam String author, Pageable pageable) {
        return service.listByAuthor(author, pageable);
    }

    @PostMapping
    public ResponseEntity<PostResponse> create(
            @Valid @RequestBody PostCreateRequest req,
            UriComponentsBuilder uri
    ) {
        PostResponse created = service.create(req);
        return ResponseEntity
                .created(uri.path("/api/posts/")
                .buildAndExpand(created.id()).toUri()
        ).body(created);
    }

    @GetMapping("/{id}")
    public PostResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    public PostResponse update(
            @PathVariable Long id,
            @Valid @RequestBody PostUpdateRequest req
    ) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
