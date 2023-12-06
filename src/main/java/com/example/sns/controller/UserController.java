package com.example.sns.controller;

import com.example.sns.controller.request.UserJoinRequest;
import com.example.sns.controller.request.UserSignInRequest;
import com.example.sns.controller.response.Response;
import com.example.sns.controller.response.UserJoinResponse;
import com.example.sns.controller.response.UserSignInResponse;
import com.example.sns.model.UserDto;
import com.example.sns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public Response<UserJoinResponse> register(@RequestBody UserJoinRequest userJoinRequest) {
        UserDto userDto = userService.register(userJoinRequest.getUserName(), userJoinRequest.getPassword());

        return Response.success(UserJoinResponse.fromUserDto(userDto));
    }

    @PostMapping("/signIn")
    public Response<UserSignInResponse> signIn(@RequestBody UserSignInRequest userSignInRequest) {
        String token = userService.signIn(userSignInRequest.getUserName(), userSignInRequest.getPassword());

        return Response.success(new UserSignInResponse(token));
    }
}
