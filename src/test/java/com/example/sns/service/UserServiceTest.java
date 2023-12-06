package com.example.sns.service;

import com.example.sns.exception.CustomException;
import com.example.sns.exception.ErrorCode;
import com.example.sns.fixture.UserFixture;
import com.example.sns.model.entity.User;
import com.example.sns.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BCryptPasswordEncoder encoder;

    @Test
    void register() {
        String userName = "wheesunglee";
        String password = "a1234";

        when(userRepository.findByUserName(userName)).thenReturn(Optional.empty());

        when(encoder.encode(password)).thenReturn("encrypted_password");

        when(userRepository.save(any())).thenReturn(UserFixture.get(userName, password));

        Assertions.assertDoesNotThrow(() -> userService.register(userName, password));
    }

    // fail to register - userName already exists
    @Test
    void userNameAlreadyExists() {
        String userName = "wheesunglee";
        String password = "a1234";

        User fixture = UserFixture.get(userName, password);

        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));

        CustomException exception = Assertions.assertThrows(CustomException.class, () -> userService.register(userName, password));

        Assertions.assertEquals(ErrorCode.DUPLICATE_USER_NAME, exception.getErrorCode());
    }

    @Test
    void signIn() {
        String userName = "wheesunglee";
        String password = "a1234";

        User fixture = UserFixture.get(userName, password);

        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));

        when(encoder.matches(password, fixture.getPassword())).thenReturn(true);

        Assertions.assertDoesNotThrow(() -> userService.signIn(userName, password));
    }

    // fail to sign in - unregistered userName
    @Test
    void userNameNotFound() {
        String userName = "wheesunglee";
        String password = "a1234";

        when(userRepository.findByUserName(userName)).thenReturn(Optional.empty());

        CustomException exception = Assertions.assertThrows(CustomException.class, () -> userService.signIn(userName, password));

        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    // fail to sign in - password not matching
    @Test
    void passwordNotMatching() {
        String userName = "wheesunglee";
        String password = "a1234";
        String incorrectPassword = "incorrectPassword";

        User fixture = UserFixture.get(userName, password);

        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));

        when(encoder.matches(password, fixture.getPassword())).thenReturn(true);

        CustomException exception = Assertions.assertThrows(CustomException.class, () -> userService.signIn(userName, incorrectPassword));

        Assertions.assertEquals(ErrorCode.INVALID_PASSWORD, exception.getErrorCode());
    }

}
