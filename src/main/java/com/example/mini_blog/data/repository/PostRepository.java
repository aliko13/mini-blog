package com.example.mini_blog.data.repository;

import com.example.mini_blog.data.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByAuthorUsername(String username, Pageable pageable);
}
