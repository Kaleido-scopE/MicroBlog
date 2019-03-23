package com.example.noah.microblog.service;

import com.example.noah.microblog.entity.UserEntity;
import com.example.noah.microblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //判断密码是否正确
    public boolean isPasswordRight(String username, String password) {
        if (userRepository.getUserCount(username) == 0)
            return false;
        else
            return userRepository.getOne(username).getPassword().equals(password);
    }

    //创建新账号
    public int createNewAccount(String username, String password, String nickname) {
        if (userRepository.getUserCount(username) != 0)//若使用username能查到用户，则注册失败
            return 1;
        else {
            UserEntity userEntity = new UserEntity(username, password, nickname, "");
            userRepository.save(userEntity);
            return 0;
        }
    }

    //修改密码
    public void setPassword(String username, String newPass) {
        UserEntity userEntity = userRepository.getOne(username);
        userEntity.setPassword(newPass);
        userRepository.save(userEntity);
    }

    //获得指定用户昵称
    public String getUserNickname(String username) {
        return userRepository.getOne(username).getNickname();
    }

    //获得指定用户头像，未设置则返回空串
    public String getUserAvatar(String username) {
        return userRepository.getOne(username).getAvatar();
    }

    //设置用户昵称
    public void setUserNickname(String username, String nickname) {
        UserEntity userEntity = userRepository.getOne(username);
        userEntity.setNickname(nickname);
        userRepository.save(userEntity);
    }

    //设置用户头像
    public void setUserAvatar(String username, String base64Img) {
        UserEntity userEntity = userRepository.getOne(username);
        userEntity.setAvatar(base64Img);
        userRepository.save(userEntity);
    }
}
