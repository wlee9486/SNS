package com.example.sns.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserSignInRequest {
    private String userName;
    private String password;


}
