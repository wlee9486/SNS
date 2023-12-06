package com.example.sns.service;

import com.example.sns.exception.CustomException;
import com.example.sns.exception.ErrorCode;
import com.example.sns.model.UserDto;
import com.example.sns.model.entity.User;
import com.example.sns.repository.UserRepository;
import com.example.sns.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expire-time-ms}")
    private long expireTimeMs;

    @Transactional
    public UserDto register(String userName, String password) {

        // Optional 값이 있을 경우 ifPresent() 실행
        userRepository.findByUserName(userName).ifPresent(user -> {
            throw new CustomException(ErrorCode.DUPLICATE_USER_NAME, String.format("%s already exists", userName));
        });

        User user = userRepository.save(new User(userName, encoder.encode(password)));

        return UserDto.fromEntity(user);
    }

    public String signIn(String userName, String password) {

        User user = userRepository.findByUserName(userName).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, String.format("%s not found", userName)));

        if (!encoder.matches(password, user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        String token = JwtTokenUtils.generateToken(userName, secretKey, expireTimeMs);

        return token;
    }
}
