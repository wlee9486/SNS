package com.example.sns.controller;

import com.example.sns.controller.request.UserJoinRequest;
import com.example.sns.controller.request.UserSignInRequest;
import com.example.sns.exception.CustomException;
import com.example.sns.exception.ErrorCode;
import com.example.sns.model.UserDto;
import com.example.sns.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    public void register() throws Exception {
        String userName = "wheesunglee";
        String password = "a1234";

        when(userService.register(userName, password)).thenReturn(mock(UserDto.class));

        mockMvc.perform(post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName, password)))
        ).andDo(print()).andExpect(status().isOk());
    }

    // fail to register
    @Test
    public void userNameAlreadyExists() throws Exception {
        String userName = "wheesunglee";
        String password = "a1234";

        when(userService.register(userName, password)).thenThrow(new CustomException(ErrorCode.DUPLICATE_USER_NAME));

        mockMvc.perform(post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName, password)))
        ).andDo(print()).andExpect(status().isConflict());
    }

    @Test
    public void signIn() throws Exception {
        String userName = "wheesunglee";
        String password = "a1234";

        when(userService.signIn(userName, password)).thenReturn("test_token");

        mockMvc.perform(post("/api/v1/users/signIn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserSignInRequest(userName, password)))
        ).andDo(print()).andExpect(status().isOk());
    }

    // fail to sign in - unregistered userName
    @Test
    public void userNameNotFound() throws Exception {
        String userName = "wheesunglee";
        String password = "a1234";

        when(userService.signIn(userName, password)).thenThrow(new CustomException(ErrorCode.USER_NOT_FOUND));

        mockMvc.perform(post("/api/v1/users/signIn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserSignInRequest(userName, password)))
        ).andDo(print()).andExpect(status().isNotFound());
    }

    // fail to sign in - password not matching
    @Test
    public void passwordNotMatching() throws Exception {
        String userName = "wheesunglee";
        String password = "a1234";

        when(userService.signIn(userName, password)).thenThrow(new CustomException(ErrorCode.INVALID_PASSWORD));

        mockMvc.perform(post("/api/v1/users/signIn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserSignInRequest(userName, password)))
        ).andDo(print()).andExpect(status().isUnauthorized());
    }

}
