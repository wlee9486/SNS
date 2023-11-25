package com.example.sns.model;


import com.example.sns.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
public class UserDto {

    private Integer id;
    private String userName;
    private String password;
    private UserRole role;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static UserDto fromEntity(User user) {
        return new UserDto(user.getId(), user.getUserName(), user.getPassword(), user.getRole(), user.getRegisteredAt(), user.getUpdatedAt(), user.getDeletedAt());
    }
}
