package com.example.mini_blog.service;

import com.example.mini_blog.data.entity.Post;
import com.example.mini_blog.data.entity.User;
import com.example.mini_blog.data.repository.PostRepository;
import com.example.mini_blog.data.repository.UserRepository;
import com.example.mini_blog.dto.post.PostCreateRequest;
import com.example.mini_blog.dto.post.PostResponse;
import com.example.mini_blog.service.converter.PostConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @InjectMocks
    PostService postService;

    @Mock
    PostRepository postRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    PostConverter postConverter;

    @Test
    void test_create_findsAuthor_CreatesPost_PersistsAndReturn() {
        // Given
        User user = mock(User.class);
        Post post = mock(Post.class);
        PostCreateRequest postCreateRequest = mock(PostCreateRequest.class);
        PostResponse postResponse = mock(PostResponse.class);

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(postConverter.toDto(post)).thenReturn(postResponse);

        // When
        PostResponse response = postService.create(postCreateRequest);

        // Then
        assertEquals(response, postResponse);
    }

}
