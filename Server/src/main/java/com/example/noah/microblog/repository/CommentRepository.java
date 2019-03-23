package com.example.noah.microblog.repository;

import com.example.noah.microblog.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    List<CommentEntity> findAllByBlogIdOrderByPublishTimeAsc(Integer blogId);
}
