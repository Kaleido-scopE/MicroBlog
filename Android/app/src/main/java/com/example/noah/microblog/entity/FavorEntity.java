package com.example.noah.microblog.entity;

public class FavorEntity {
    private Integer blogId;//对应博客
    private String username;//点赞的用户

    public FavorEntity() {
    }

    public FavorEntity(Integer blogId, String username) {
        this.blogId = blogId;
        this.username = username;
    }

    public Integer getBlogId() {
        return blogId;
    }

    public void setBlogId(Integer blogId) {
        this.blogId = blogId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
