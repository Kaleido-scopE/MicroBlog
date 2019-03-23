package com.example.noah.microblog.entity;

import javax.persistence.*;

@Entity
@Table(name = "microblog_info")
public class MicroBlogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;//所属用户
    @Column(columnDefinition = "text")
    private String content;//微博内容
    @Column(columnDefinition = "text")
    private String contentPic;//微博图片
    private Integer favorCount;//点赞数
    private Long publishTime;//发表时间

    public MicroBlogEntity() {
    }

    public MicroBlogEntity(String username, String content, String contentPic, Integer favorCount, Long publishTime) {
        this.username = username;
        this.content = content;
        this.contentPic = contentPic;
        this.favorCount = favorCount;
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

    public Long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Long publishTime) {
        this.publishTime = publishTime;
    }
}
