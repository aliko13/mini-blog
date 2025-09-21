package com.example.mini_blog.service;

import com.example.mini_blog.data.entity.User;
import com.example.mini_blog.data.repository.UserRepository;
import com.example.mini_blog.dto.user.UserCreateRequest;
import com.example.mini_blog.dto.user.UserResponse;
import com.example.mini_blog.dto.user.UserUpdateRequest;
import com.example.mini_blog.service.converter.UserConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserConverter userConverter;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService service;

    @Test
    void create_persistsAndReturnsDto() {
        // Given
        var req = new UserCreateRequest("ali", "a@x.com");
        var convertedUser = mock(User.class);
        var persistedUser = mock(User.class);

        when(userConverter.toEntity(req)).thenReturn(convertedUser);
        when(userRepository.save(convertedUser)).thenReturn(persistedUser);
        when(userConverter.toDto(persistedUser))
                .thenReturn(new UserResponse(3L, "ali", "a@x.com"));

        // When
        UserResponse res = service.create(req);

        // Then
        assertThat(res.id()).isEqualTo(3L);
        assertThat(res.username()).isEqualTo("ali");
        assertThat(res.email()).isEqualTo("a@x.com");

        verify(userConverter).toDto(persistedUser);
    }

    @Test
    void get_missing_throws() {
        when(userRepository.findById(9L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.get(9L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User not found: 9");
    }

    @Test
    void list_returnsPage() {
        var pageable = PageRequest.of(0,10);
        var page = new PageImpl<>(List.of(
                User.builder().id(1L).username("a").build(),
                User.builder().id(2L).username("b").build()
        ));
        when(userRepository.findAll(pageable)).thenReturn(page);

        Page<UserResponse> res = service.list(pageable);
        assertThat(res.getContent().size()).isEqualTo(2);
    }

    @Test
    void get_success_returnsDto() {
        var entity = User.builder().id(5L).username("eva").email("e@x.com").build();
        when(userRepository.findById(5L)).thenReturn(Optional.of(entity));
        when(userConverter.toDto(entity)).thenReturn(new UserResponse(5L, "eva", "e@x.com"));

        UserResponse res = service.get(5L);

        assertThat(res.id()).isEqualTo(5L);
        assertThat(res.username()).isEqualTo("eva");
        assertThat(res.email()).isEqualTo("e@x.com");
        verify(userRepository).findById(5L);
        verify(userConverter).toDto(entity);
    }

    @Test
    void update_success_updatesAndReturnsDto() {
        var existing = User.builder().id(7L).username("old").email("old@x.com").build();
        var req = new UserUpdateRequest("new", "new@x.com");
        var saved = User.builder().id(7L).username("new").email("new@x.com").build();

        when(userRepository.findById(7L)).thenReturn(Optional.of(existing));
        when(userRepository.save(Mockito.<User>any())).thenReturn(saved);
        when(userConverter.toDto(saved)).thenReturn(new UserResponse(7L, "new", "new@x.com"));

        UserResponse res = service.update(7L, req);

        assertThat(res.id()).isEqualTo(7L);
        assertThat(res.username()).isEqualTo("new");
        assertThat(res.email()).isEqualTo("new@x.com");
        verify(userRepository).findById(7L);
        verify(userRepository).save(existing); // same reference updated by the service
        verify(userConverter).toDto(saved);
    }

    @Test
    void update_missing_throws() {
        when(userRepository.findById(77L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(77L, new UserUpdateRequest("x","y@z.com")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User not found: 77");

        verify(userRepository).findById(77L);
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(userConverter);
    }

    @Test
    void delete_success_callsRepo() {
        when(userRepository.existsById(10L)).thenReturn(true);

        service.delete(10L);

        verify(userRepository).existsById(10L);
        verify(userRepository).deleteById(10L);
    }

    @Test
    void delete_missing_throws() {
        when(userRepository.existsById(11L)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(11L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User not found: 11");

        verify(userRepository).existsById(11L);
        verify(userRepository, never()).deleteById(anyLong());
        verifyNoInteractions(userConverter);
    }
}
