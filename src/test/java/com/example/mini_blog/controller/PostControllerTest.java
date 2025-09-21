package com.example.mini_blog.controller;

import com.example.mini_blog.dto.post.PostCreateRequest;
import com.example.mini_blog.dto.post.PostResponse;
import com.example.mini_blog.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper om;

    @MockitoBean
    PostService postService;

    @Test
    void create_returns201_and_body() throws Exception {
        var req = new PostCreateRequest("Hello", "World", 1L);
        var res = new PostResponse(10L, "Hello", "World", 1L, "ali", Instant.now(), Instant.now());

        when(postService.create(any())).thenReturn(res);

        mvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", Matchers.endsWith("/api/posts/")))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.authorUsername").value("ali"));
    }

    @Test
    void list_paginates() throws Exception {
        var page = new PageImpl<>(List.of(
                new PostResponse(1L, "A", "...", 1L, "ali", Instant.now(), Instant.now()),
                new PostResponse(2L, "B", "...", 1L, "ali", Instant.now(), Instant.now())
        ), PageRequest.of(0,2), 5);

        when(postService.list(any())).thenReturn(page);

        mvc.perform(get("/api/posts?page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.totalElements").value(5));
    }

    @Test
    void get_notFound_returns404() throws Exception {
        when(postService.get(99L)).thenThrow(new IllegalArgumentException("Post not found: 99"));

        mvc.perform(get("/api/posts/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Post not found: 99"));
    }
}

























