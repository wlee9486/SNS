package com.example.sns.service;

import com.example.sns.exception.CustomException;
import com.example.sns.fixture.UserEntityFixture;
import com.example.sns.model.entity.User;
import com.example.sns.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserDtoServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void register() {
        String userName = "wheesunglee";
        String password = "a1234";

        when(userRepository.findByUserName(userName)).thenReturn(Optional.empty());

        when(userRepository.save(any())).thenReturn(Optional.of(UserEntityFixture.get(userName, password)));

        Assertions.assertDoesNotThrow(() -> userService.register(userName, password));
    }

    // fail to register - userName already exists
    @Test
    void userNameAlreadyExists() {
        String userName = "wheesunglee";
        String password = "a1234";

        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(UserEntityFixture.get(userName, password)));

        Assertions.assertThrows(CustomException.class, () -> userService.register(userName, password));
    }

    @Test
    void signIn() {
        String userName = "wheesunglee";
        String password = "a1234";

        User fixture = UserEntityFixture.get(userName, password);

        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));

        Assertions.assertDoesNotThrow(() -> userService.signIn(userName, password));
    }

    // fail to sign in - unregistered userName
    @Test
    void userNameNotFound() {
        String userName = "wheesunglee";
        String password = "a1234";

        when(userRepository.findByUserName(userName)).thenReturn(Optional.empty());

        Assertions.assertThrows(CustomException.class, () -> userService.signIn(userName, password));
    }

    // fail to sign in - password not matching
    @Test
    void passwordNotMatching() {
        String userName = "wheesunglee";
        String password = "a1234";
        String incorrectPassword = "incorrectPassword";

        User fixture = UserEntityFixture.get(userName, password);

        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));

        Assertions.assertThrows(CustomException.class, () -> userService.signIn(userName, incorrectPassword));
    }

}
