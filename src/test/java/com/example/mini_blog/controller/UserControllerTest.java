package com.example.mini_blog.controller;

import com.example.mini_blog.dto.user.UserCreateRequest;
import com.example.mini_blog.dto.user.UserResponse;
import com.example.mini_blog.service.UserService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper om;

    @MockitoBean
    UserService userService;

    @Test
    void create_returns201() throws Exception {
        var req = new UserCreateRequest("ali", "ali@example.com");
        var res = new UserResponse(5L, "ali", "ali@example.com");

        when(userService.create(any())).thenReturn(res);

        mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", Matchers.endsWith("/api/users/5")))
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.username").value("ali"));
    }

    @Test
    void list_returnsPage() throws Exception {
        var page = new PageImpl<>(List.of(
                new UserResponse(1L, "ali", "a@x.com"),
                new UserResponse(2L, "mina", "m@x.com")
        ), PageRequest.of(0,2), 2);

        when(userService.list(any())).thenReturn(page);

        mvc.perform(get("/api/users?page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[1].username").value("mina"));
    }

    @Test
    void get_notFound_returns404() throws Exception {
        when(userService.get(42L)).thenThrow(new IllegalArgumentException("User not found: 42"));

        mvc.perform(get("/api/users/42"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User not found: 42"));
    }

}
