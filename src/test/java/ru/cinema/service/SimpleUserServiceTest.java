package ru.cinema.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.cinema.model.User;
import ru.cinema.repository.user.UserRepository;
import ru.cinema.service.user.SimpleUserService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleUserServiceTest {
    private UserRepository userRepository;
    private SimpleUserService userService;

    @BeforeEach
    public void init() {
        userRepository = mock(UserRepository.class);
        userService = new SimpleUserService(userRepository);
    }

    @Test
    public void whenSaveThenReturnSavedUser() {
        var user = new User(0, "Ivan", "ivan@test.com", "123");
        when(userRepository.save(user)).thenReturn(Optional.of(user));

        var saved = userService.save(user);

        assertThat(saved).isPresent();
        assertThat(saved.get().getEmail()).isEqualTo("ivan@test.com");
    }

    @Test
    public void whenFindByEmailAndPasswordThenReturnUser() {
        var user = new User(1, "Ivan", "ivan@test.com", "123");
        when(userRepository.findByEmailAndPassword("ivan@test.com", "123"))
                .thenReturn(Optional.of(user));

        var found = userService.findByEmailAndPassword("ivan@test.com", "123");

        assertThat(found).isPresent();
        assertThat(found.get().getFullName()).isEqualTo("Ivan");
    }
}
