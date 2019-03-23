package com.example.noah.microblog.service;

import com.example.noah.microblog.entity.MicroBlogEntity;
import com.example.noah.microblog.repository.MicroBlogRepository;
import com.example.noah.microblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MicroBlogService {
    private final MicroBlogRepository microBlogRepository;
    private final UserRepository userRepository;

    @Autowired
    public MicroBlogService(MicroBlogRepository microBlogRepository, UserRepository userRepository) {
        this.microBlogRepository = microBlogRepository;
        this.userRepository = userRepository;
    }

    //获得指定微博的详情
    public MicroBlogEntity getSpecifiedBlog(Integer blogId) {
        return microBlogRepository.getOne(blogId);
    }

    //获得微博信息列表
    public List<MicroBlogEntity> getAllMicroBlog() {
        return microBlogRepository.findAllByOrderByPublishTimeDesc();
    }

    //发布新微博
    public void postNewMicroBlog(String username, String content, String contentPic) {
        MicroBlogEntity microBlogEntity = new MicroBlogEntity();
        microBlogEntity.setUsername(username);
        microBlogEntity.setContent(content);
        microBlogEntity.setContentPic(contentPic);
        microBlogEntity.setFavorCount(0);
        microBlogEntity.setPublishTime(System.currentTimeMillis());//获取当前系统时间
        microBlogRepository.save(microBlogEntity);
    }

    //点赞微博
    public void favorMicroBlog(Integer blogId) {
        MicroBlogEntity microBlogEntity = microBlogRepository.getOne(blogId);
        microBlogEntity.setFavorCount(microBlogEntity.getFavorCount() + 1);
        microBlogRepository.save(microBlogEntity);
    }

    //转发微博
    public void relayMicroBlog(String replayer, Integer originBlogId) {
        MicroBlogEntity microBlogEntity = microBlogRepository.getOne(originBlogId);
        //为新内容附加来源头部
        String newContent = "Origin —— " + userRepository.getOne(microBlogEntity.getUsername()).getNickname() + "\n\n" + microBlogEntity.getContent();
        MicroBlogEntity newMicroBlogEntity = new MicroBlogEntity(replayer, newContent, microBlogEntity.getContentPic(), 0, System.currentTimeMillis());
        microBlogRepository.save(newMicroBlogEntity);
    }
}
