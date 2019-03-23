package com.example.noah.microblog.repository;

import com.example.noah.microblog.entity.MicroBlogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MicroBlogRepository extends JpaRepository<MicroBlogEntity, Integer> {
    List<MicroBlogEntity> findAllByOrderByPublishTimeDesc();
}
