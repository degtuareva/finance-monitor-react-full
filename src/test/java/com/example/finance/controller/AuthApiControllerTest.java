package com.example.finance.controller;

import com.example.finance.dto.RegistrationRequest;
import com.example.finance.entity.User;
import com.example.finance.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthApiController.class)
class AuthApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService users;

    @MockBean
    private SecurityFilterChain securityFilterChain;

    @Test
    void invalidRegistrationIsBadRequest() throws Exception {
        mvc.perform(
                        post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void validRegistrationIsCreated() throws Exception {
        String json = """
            {
              "name": "Test",
              "email": "test@example.com",
              "password": "123456",
              "confirmPassword": "123456"
            }
            """;

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail("test@example.com");
        savedUser.setRegistrationDate(LocalDate.now());

        when(users.register(any(RegistrationRequest.class)))
                .thenReturn(savedUser);
        mvc.perform(
                        post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isCreated());
    }
}