package com.example.sns.fixture;

import com.example.sns.model.entity.User;

public class UserFixture {

    public static User get(String userName, String password) {
//        User result = new User();
//        result.setId(1);
//        result.setUserName(userName);
//        result.setPassword(password);

        User result = new User(userName, password);

        return result;
    }
}
