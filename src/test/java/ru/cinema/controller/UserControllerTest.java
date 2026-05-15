package ru.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import ru.cinema.model.User;
import ru.cinema.service.UserService;

import java.util.Optional;

import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class UserControllerTest {
    private UserService userService;
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        userService = mock(UserService.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new UserController(userService))
                .setViewResolvers(viewResolver())
                .build();
    }

    @Test
    public void whenGetRegisterThenReturnRegisterView() throws Exception {
        mockMvc.perform(get("/users/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    public void whenRegisterSuccessThenRedirectToLogin() throws Exception {
        when(userService.save(any(User.class))).thenReturn(Optional.of(new User()));

        mockMvc.perform(post("/users/register")
                        .param("fullName", "Ivan")
                        .param("email", "ivan@test.com")
                        .param("password", "123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/login"));
    }

    @Test
    public void whenRegisterFailsThenReturnRegisterView() throws Exception {
        when(userService.save(any(User.class))).thenReturn(Optional.empty());

        mockMvc.perform(post("/users/register")
                        .param("fullName", "Ivan")
                        .param("email", "ivan@test.com")
                        .param("password", "123"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(view().name("register"));
    }

    @Test
    public void whenLoginSuccessThenSaveUserInSession() throws Exception {
        var user = new User(1, "Ivan", "ivan@test.com", "123");
        when(userService.findByEmailAndPassword("ivan@test.com", "123"))
                .thenReturn(Optional.of(user));

        mockMvc.perform(post("/users/login")
                        .param("email", "ivan@test.com")
                        .param("password", "123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(request().sessionAttribute("user", instanceOf(User.class)))
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void whenLoginFailsThenReturnLoginView() throws Exception {
        when(userService.findByEmailAndPassword("bad@test.com", "123"))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/users/login")
                        .param("email", "bad@test.com")
                        .param("password", "123"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(view().name("login"));
    }

    @Test
    public void whenLogoutThenRedirectToIndex() throws Exception {
        mockMvc.perform(get("/users/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    private InternalResourceViewResolver viewResolver() {
        return new InternalResourceViewResolver("/WEB-INF/", ".jsp");
    }
}
