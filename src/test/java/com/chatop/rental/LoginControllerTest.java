package com.chatop.rental;

import com.chatop.rental.dto.LoginRequest;
import com.chatop.rental.dto.UserDTO;
import com.chatop.rental.services.CustomUserDetailsService;
import com.chatop.rental.services.JWTService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;

@SpringBootTest
@AutoConfigureMockMvc

public class LoginControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String asJsonString(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }

    @Test
    public void shouldReturnDefaultMessage() throws Exception {
        mockMvc.perform(get("/auth/me")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void userLoginTest() throws Exception {
        mockMvc.perform(formLogin("/auth/login").user("user@test.fr").password("user")).andExpect(authenticated());
    }

    @Test
    public void userLoginFailed() throws Exception {
        mockMvc.perform(formLogin("/auth/login").user("user@test.fr").password("wrong")).andExpect(authenticated());
    }

    @Test
    @WithMockUser
    public void shouldReturn() throws Exception {
        mockMvc.perform(get("/auth/me")).andDo(print()).andExpect(status().isOk());
    }

}
