package com.example.sns.model.entity;

import com.example.sns.model.UserRole;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "\"user\"")
@Getter
//@Setter
@SQLDelete(sql = "UPDATE \"user\" SET deleted_at = NOW() where id = ?") // soft delete
@Where(clause = "deleted_at is NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @PrePersist // 엔티티가 데이터베이스에 저장되기 전에 실행되어야 하는 메서드
    void registeredAt() {
        this.registeredAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

//    public static User of(String userName, String password) {
//        User user = new User();
//        user.setUserName(userName);
//        user.setPassword(password);
//
//        return user;
//    }

    @Builder
    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

}
