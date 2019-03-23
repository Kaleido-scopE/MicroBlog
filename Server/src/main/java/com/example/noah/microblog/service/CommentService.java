package com.example.noah.microblog.service;

import com.example.noah.microblog.entity.CommentEntity;
import com.example.noah.microblog.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    //获得指定微博id对应的所有评论
    public List<CommentEntity> getCommentsByMicroBlogId(Integer blogId) {
        return commentRepository.findAllByBlogIdOrderByPublishTimeAsc(blogId);
    }

    //在指定微博下发表评论
    public void postNewComment(String username, String content, Integer blogId) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setUsername(username);
        commentEntity.setContent(content);
        commentEntity.setBlogId(blogId);
        commentEntity.setPublishTime(System.currentTimeMillis());
        commentRepository.save(commentEntity);
    }
}
