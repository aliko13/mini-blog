package com.example.mini_blog.controller;

import com.example.mini_blog.dto.post.PostCreateRequest;
import com.example.mini_blog.dto.post.PostResponse;
import com.example.mini_blog.service.PostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
public class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    PostService service;

    // CRUD Tests
    @Test
    void create_post_returns_201_status_and_body() throws Exception {
        long userId = 1L;
        long postId = 1L;
        String userName = "testUser";
        Instant createdAt = Instant.now();
        Instant updatedAt = null;
        PostCreateRequest postCreateRequest = new PostCreateRequest("News", "News Feed", userId);
        PostResponse postResponse = new PostResponse(postId, "News", "News Feed", userId, userName, createdAt, updatedAt);

        when(service.create(postCreateRequest)).thenReturn(postResponse);

        mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("News"));
    }

    @Test
    void list_pagination() throws Exception {
        long userId = 1L;
        long postId = 1L;
        String userName = "testUser";
        Instant createdAt = Instant.now();
        Instant updatedAt = null;

        PostResponse postResponse1 =
                new PostResponse(postId, "News", "News Feed", userId, userName, createdAt, updatedAt);
        PostResponse postResponse2  =
                new PostResponse(postId, "News", "News Feed", userId, userName, createdAt, updatedAt);

        PageImpl<PostResponse> pageableResponse =
                new PageImpl<>(List.of(postResponse1, postResponse2), PageRequest.of(0, 2), 5);

        when(service.list(any())).thenReturn(pageableResponse);

        mockMvc.perform(get("/api/posts?page=0&size2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(postId));
    }
}

























