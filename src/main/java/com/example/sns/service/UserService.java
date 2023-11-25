package com.example.sns.service;

import com.example.sns.exception.CustomException;
import com.example.sns.exception.ErrorCode;
import com.example.sns.model.UserDto;
import com.example.sns.model.entity.User;
import com.example.sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto register(String userName, String password) {

        userRepository.findByUserName(userName).ifPresent(user -> {
            new CustomException(ErrorCode.DUPLICATE_USER_NAME, String.format("%s is duplicated", userName));
        });

        User user = userRepository.save(User.of(userName, password));

        return UserDto.fromEntity(user);
    }

    public String signIn(String userName, String password) {

        User user = userRepository.findByUserName(userName).orElseThrow(() -> new CustomException(ErrorCode.DUPLICATE_USER_NAME, ""));

        if (!user.getPassword().equals(password)) {
            throw new CustomException(ErrorCode.DUPLICATE_USER_NAME, "");
        }

        return "";
    }
}
