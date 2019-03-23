package com.example.noah.microblog.entity;

public class CommentEntity {
    private String username;//所属用户
    private String nickname;//用户昵称
    private String content;//微博内容
    private Long publishTime;//发表时间
    private Integer blogId;//所属微博的Id

    public CommentEntity() {
    }

    public CommentEntity(String username, String nickname, String content, Long publishTime, Integer blogId) {
        this.username = username;
        this.nickname = nickname;
        this.content = content;
        this.publishTime = publishTime;
        this.blogId = blogId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Long publishTime) {
        this.publishTime = publishTime;
    }

    public Integer getBlogId() {
        return blogId;
    }

    public void setBlogId(Integer blogId) {
        this.blogId = blogId;
    }
}
