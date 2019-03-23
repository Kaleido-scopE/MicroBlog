package com.example.noah.microblog.utils;

public class StatusManager {
    private static Boolean isLogin;
    private static String username;
    private static String password;
    private static String nickname;

    public static void setIsLogin(Boolean isLogin) {
        StatusManager.isLogin = isLogin;
    }

    public static Boolean getIsLogin() {
        return isLogin;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        StatusManager.username = username;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        StatusManager.password = password;
    }

    public static String getNickname() {
        return nickname;
    }

    public static void setNickname(String nickname) {
        StatusManager.nickname = nickname;
    }
}
