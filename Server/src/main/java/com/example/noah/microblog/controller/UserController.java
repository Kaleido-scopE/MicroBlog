package com.example.noah.microblog.controller;

import com.example.noah.microblog.form.CommonResponseForm;
import com.example.noah.microblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //注册
    @PostMapping(value = "/register")
    public CommonResponseForm register(@RequestBody Map<String, Object> request) {
        String username = (String) request.get("username");
        String password = (String) request.get("password");
        String nickname = (String) request.get("nickname");

        int status = userService.createNewAccount(username, password, nickname);
        return new CommonResponseForm(status, status == 1 ? "The username exists!" : "Register Successfully!", null);
    }

    //登录
    @PostMapping(value = "/login")
    public CommonResponseForm login(@RequestBody Map<String, Object> request) {
        String username = (String) request.get("username");
        String password = (String) request.get("password");

        if (userService.isPasswordRight(username, password))
            return new CommonResponseForm(0, "Login Successfully!", userService.getUserNickname(username));//状态码为0，验证通过，返回昵称
        else
            return new CommonResponseForm(1, "Wrong Password or Nonexistent User!", null);//状态码为1，验证不通过
    }

    //修改密码
    @PostMapping(value = "/modifyPassword")
    public CommonResponseForm modifyPassword(@RequestBody Map<String, Object> request) {
        String username = (String) request.get("username");
        String currentPassword = (String) request.get("password");
        String newPassword = (String) request.get("newPassword");

        //验证身份
        if (userService.isPasswordRight(username, currentPassword)) {
            userService.setPassword(username, newPassword);
            return new CommonResponseForm(0, "Set Password Successfully!", null);
        }
        else
            return new CommonResponseForm(1, "Illegal Request!", null);
    }

    //获得用户昵称
    @PostMapping(value = "/getUserNickname")
    public CommonResponseForm getUserNickname(@RequestBody Map<String, Object> request) {
        String username = (String) request.get("username");
        return new CommonResponseForm(0, "Get User Nickname.", userService.getUserNickname(username));
    }

    //获得用户头像
    @PostMapping(value = "/getUserAvatar")
    public CommonResponseForm getUserAvatar(@RequestBody Map<String, Object> request) {
        String username = (String) request.get("username");
        return new CommonResponseForm(0, "Get User Avatar.", userService.getUserAvatar(username));
    }

    //更改用户昵称
    @PostMapping(value = "/setUserNickname")
    public CommonResponseForm setUserNickname(@RequestBody Map<String, Object> request) {
        String username = (String) request.get("username");
        String password = (String) request.get("password");
        String newNickname = (String) request.get("newNickname");

        //验证身份
        if (userService.isPasswordRight(username, password)) {
            userService.setUserNickname(username, newNickname);
            return new CommonResponseForm(0, "Set Nickname Successfully!", null);
        }
        else
            return new CommonResponseForm(1, "Illegal Request!", null);
    }

    //更改用户头像
    @PostMapping(value = "/setUserAvatar")
    public CommonResponseForm setUserAvatar(@RequestBody Map<String, Object> request) {
        String username = (String) request.get("username");
        String password = (String) request.get("password");
        String base64Avatar = (String) request.get("base64Avatar");

        //验证身份
        if (userService.isPasswordRight(username, password)) {
            userService.setUserAvatar(username, base64Avatar);
            return new CommonResponseForm(0, "Set Avatar Successfully!", null);
        }
        else
            return new CommonResponseForm(1, "Illegal Request!", null);
    }
}
