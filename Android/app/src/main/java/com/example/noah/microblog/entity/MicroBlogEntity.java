package com.example.noah.microblog.entity;

import java.io.Serializable;
import java.util.List;

public class MicroBlogEntity implements Serializable {
    private Integer id;
    private String username;//所属用户
    private String nickname;//用户昵称
    private String content;//微博内容
    private String contentPic;//微博图片
    private Integer favorCount;//点赞数
    private List<String> favorUsers;//点赞用户列表
    private Boolean clickable;//是否可点击
    private Long publishTime;//发表时间

    public MicroBlogEntity() {
    }

    public MicroBlogEntity(Integer id, String username, String nickname, String content, String contentPic, Integer favorCount, List<String> favorUsers, Boolean clickable, Long publishTime) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.content = content;
        this.contentPic = contentPic;
        this.favorCount = favorCount;
        this.favorUsers = favorUsers;
        this.clickable = clickable;
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

    public String getContentPic() {
        return contentPic;
    }

    public void setContentPic(String contentPic) {
        this.contentPic = contentPic;
    }

    public Integer getFavorCount() {
        return favorCount;
    }

    public void setFavorCount(Integer favorCount) {
        this.favorCount = favorCount;
    }

    public List<String> getFavorUsers() {
        return favorUsers;
    }

    public void setFavorUsers(List<String> favorUsers) {
        this.favorUsers = favorUsers;
    }

    public Boolean getClickable() {
        return clickable;
    }

    public void setClickable(Boolean clickable) {
        this.clickable = clickable;
    }

    public Long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Long publishTime) {
        this.publishTime = publishTime;
    }
}
