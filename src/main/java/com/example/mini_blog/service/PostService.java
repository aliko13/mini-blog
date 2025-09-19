package com.example.mini_blog.service;

import com.example.mini_blog.data.entity.Post;
import com.example.mini_blog.data.entity.User;
import com.example.mini_blog.data.repository.PostRepository;
import com.example.mini_blog.data.repository.UserRepository;
import com.example.mini_blog.dto.post.PostCreateRequest;
import com.example.mini_blog.dto.post.PostResponse;
import com.example.mini_blog.dto.post.PostUpdateRequest;
import com.example.mini_blog.service.converter.PostConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository posts;
    private final UserRepository users;
    private final PostConverter postConverter;

    public Page<PostResponse> list(Pageable pageable) {
        return posts.findAll(pageable).map(postConverter::toDto);
    }

    public Page<PostResponse> listByAuthor(String username, Pageable pageable) {
        return posts.findByAuthorUsername(username, pageable).map(postConverter::toDto);
    }

    public PostResponse create(PostCreateRequest req) {
        User author = users.findById(req.authorId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + req.authorId()));
        Post p = Post.builder()
                .title(req.title())
                .content(req.content())
                .author(author)
                .build();
        return postConverter.toDto(posts.save(p));
    }

    public PostResponse get(Long id) {
        return posts.findById(id)
                .map(postConverter::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Post not found: " + id));
    }

    public PostResponse update(Long id, PostUpdateRequest req) {
        Post p = posts.findById(id).orElseThrow(() -> new IllegalArgumentException("Post not found: " + id));
        p.setTitle(req.title());
        p.setContent(req.content());
        return postConverter.toDto(posts.save(p));
    }

    public void delete(Long id) {
        if (!posts.existsById(id)) {
            throw new IllegalArgumentException("Post not found: " + id);
        }
        posts.deleteById(id);
    }
}



















