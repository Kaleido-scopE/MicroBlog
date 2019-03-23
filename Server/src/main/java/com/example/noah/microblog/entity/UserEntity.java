package com.example.noah.microblog.entity;

import javax.persistence.*;

@Entity
@Table(name = "user_info")
public class UserEntity {
    @Id
    private String username;//用户名
    private String password;//密码
    private String nickname;//昵称
    @Column(columnDefinition = "text")
    private String avatar;//头像，Base64存储

    public UserEntity() {

    }

    public UserEntity(String username, String password, String nickname, String avatar) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
