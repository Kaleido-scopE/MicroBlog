package com.example.noah.microblog.repository;

import com.example.noah.microblog.entity.FavorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavorRepository extends JpaRepository<FavorEntity, Integer> {
    List<FavorEntity> findAllByBlogId(Integer blogId);
    List<FavorEntity> findAllByUsername(String username);
}
