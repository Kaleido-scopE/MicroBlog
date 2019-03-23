package com.example.noah.microblog.repository;

import com.example.noah.microblog.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    @Query("select count(u) from UserEntity u where u.username = ?1")
    Integer getUserCount(String username);
}
