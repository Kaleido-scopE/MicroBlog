package com.example.noah.microblog.service;

import com.example.noah.microblog.entity.FavorEntity;
import com.example.noah.microblog.repository.FavorRepository;
import com.example.noah.microblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FavorService {
    private final FavorRepository favorRepository;
    private final UserRepository userRepository;

    @Autowired
    public FavorService(FavorRepository favorRepository, UserRepository userRepository) {
        this.favorRepository = favorRepository;
        this.userRepository = userRepository;
    }

    //新建一条记录
    public void createItem(Integer blogId, String username) {
        FavorEntity favorEntity = new FavorEntity();
        favorEntity.setBlogId(blogId);
        favorEntity.setUsername(username);
        favorRepository.save(favorEntity);
    }

    //查询指定微博的所有点赞者昵称
    public List<String> findUserNicknameByBlogId(Integer blogId) {
        List<String> result = new ArrayList<>();
        List<FavorEntity> favorEntities = favorRepository.findAllByBlogId(blogId);

        for (FavorEntity entity : favorEntities)
            result.add(userRepository.getOne(entity.getUsername()).getNickname());

        return result;
    }

    //查询指定用户点赞的所有微博Id
    public Set<Integer> findFavorBlogsByUsername(String username) {
        List<Integer> result = new ArrayList<>();
        List<FavorEntity> favorEntities = favorRepository.findAllByUsername(username);

        for (FavorEntity entity : favorEntities)
            result.add(entity.getBlogId());

        return new HashSet<>(result);
    }
}
