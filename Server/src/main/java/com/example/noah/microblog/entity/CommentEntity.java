package com.example.noah.microblog.entity;

import javax.persistence.*;

@Entity
@Table(name = "comment_info")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;//评论者用户名
    @Column(columnDefinition = "text")
    private String content;//评论内容
    private Integer blogId;//所属微博的Id
    private Long publishTime;//评论发布时间

    public CommentEntity() {
    }

    public CommentEntity(String username, String content, Integer blogId, Long publishTime) {
        this.username = username;
        this.content = content;
        this.blogId = blogId;
        this.publishTime = publishTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getBlogId() {
        return blogId;
    }

    public void setBlogId(Integer blogId) {
        this.blogId = blogId;
    }

    public Long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Long publishTime) {
        this.publishTime = publishTime;
    }
}
