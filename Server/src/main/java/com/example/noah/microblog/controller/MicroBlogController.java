package com.example.noah.microblog.controller;

import com.example.noah.microblog.entity.CommentEntity;
import com.example.noah.microblog.entity.MicroBlogEntity;
import com.example.noah.microblog.form.CommonResponseForm;
import com.example.noah.microblog.service.CommentService;
import com.example.noah.microblog.service.FavorService;
import com.example.noah.microblog.service.MicroBlogService;
import com.example.noah.microblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class MicroBlogController {
    private final MicroBlogService microBlogService;
    private final CommentService commentService;
    private final FavorService favorService;
    private final UserService userService;

    @Autowired
    public MicroBlogController(MicroBlogService microBlogService, CommentService commentService, FavorService favorService, UserService userService) {
        this.microBlogService = microBlogService;
        this.commentService = commentService;
        this.favorService = favorService;
        this.userService = userService;
    }

    //获取指定Id微博的详情
    @PostMapping(value = "/getBlogById")
    public CommonResponseForm getBlogById(@RequestBody Map<String, Object> request) {
        String username = (String) request.get("username");
        Integer blogId = (Integer) request.get("blogId");

        MicroBlogEntity microBlogEntity = microBlogService.getSpecifiedBlog(blogId);

        Map<String, Object> response = new HashMap<>();
        Set<Integer> favorSet = new HashSet<>();
        if (username.length() > 0)
            favorSet = favorService.findFavorBlogsByUsername(username);

        response.put("id", microBlogEntity.getId());
        response.put("username", microBlogEntity.getUsername());
        response.put("nickname", userService.getUserNickname(microBlogEntity.getUsername()));
        response.put("content", microBlogEntity.getContent());
        response.put("contentPic", microBlogEntity.getContentPic());
        response.put("favorCount", microBlogEntity.getFavorCount());
        response.put("favorUsers", favorService.findUserNicknameByBlogId(microBlogEntity.getId()));
        response.put("clickable", !favorSet.contains(microBlogEntity.getId()));
        response.put("publishTime", microBlogEntity.getPublishTime());

        return new CommonResponseForm(0, "Get Specified Blog", response);
    }

    //获取微博列表
    @PostMapping(value = "/getBlogList")
    public CommonResponseForm getBlogList(@RequestBody Map<String, Object> request) {
        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> item;
        Set<Integer> favorSet = new HashSet<>();

        String username = (String) request.get("username");
        if (username.length() > 0)
             favorSet = favorService.findFavorBlogsByUsername(username);


        //返回所有微博的基本信息和评论信息
        List<MicroBlogEntity> blogList = microBlogService.getAllMicroBlog();
        for (MicroBlogEntity microBlogEntity : blogList) {
            item = new HashMap<>();
            item.put("id", microBlogEntity.getId());
            item.put("username", microBlogEntity.getUsername());
            item.put("nickname", userService.getUserNickname(microBlogEntity.getUsername()));
            item.put("content", microBlogEntity.getContent());
            item.put("contentPic", microBlogEntity.getContentPic());
            item.put("favorCount", microBlogEntity.getFavorCount());
            item.put("favorUsers", favorService.findUserNicknameByBlogId(microBlogEntity.getId()));
            item.put("clickable", !favorSet.contains(microBlogEntity.getId()));
            item.put("publishTime", microBlogEntity.getPublishTime());
            data.add(item);
        }
        return new CommonResponseForm(0, "Get Blog List.", data);
    }

    //获得指定微博的评论列表
    @PostMapping(value = "/getBlogComment")
    public CommonResponseForm getBlogComment(@RequestBody Map<String, Object> request) {
        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> item;

        Integer blogId = (Integer) request.get("blogId");

        //获得指定微博的所有评论信息
        List<CommentEntity> commentList = commentService.getCommentsByMicroBlogId(blogId);
        for (CommentEntity commentEntity : commentList) {
            item = new HashMap<>();
            item.put("username", commentEntity.getUsername());
            item.put("nickname", userService.getUserNickname(commentEntity.getUsername()));
            item.put("content", commentEntity.getContent());
            item.put("publishTime", commentEntity.getPublishTime());
            item.put("blogId", commentEntity.getBlogId());
            data.add(item);
        }
        return new CommonResponseForm(0, "Get Blog Comment.", data);
    }

    //发表微博
    @PostMapping(value = "/postMicroBlog")
    public CommonResponseForm postMicroBlog(@RequestBody Map<String, Object> request) {
        String username = (String) request.get("username");
        String password = (String) request.get("password");
        String content = (String) request.get("content");
        String contentPic = (String) request.get("contentPic");

        //验证身份
        if (userService.isPasswordRight(username, password)) {
            microBlogService.postNewMicroBlog(username, content, contentPic);
            return new CommonResponseForm(0, "Post MicroBlog Successfully!", null);
        }
        else
            return new CommonResponseForm(1, "Illegal Request!", null);
    }

    //发表评论
    @PostMapping(value = "/postComment")
    public CommonResponseForm postComment(@RequestBody Map<String, Object> request) {
        String username = (String) request.get("username");
        String password = (String) request.get("password");
        String content = (String) request.get("content");
        Integer blogId = (Integer) request.get("blogId");

        //验证身份
        if (userService.isPasswordRight(username, password)) {
            commentService.postNewComment(username, content, blogId);
            return new CommonResponseForm(0, "Post Comment Successfully!", null);
        }
        else
            return new CommonResponseForm(1, "Illegal Request!", null);
    }

    //点赞微博
    @PostMapping(value = "/favorMicroBlog")
    public CommonResponseForm favorMicroBlog(@RequestBody Map<String, Object> request) {
        String username = (String) request.get("username");
        String password = (String) request.get("password");
        Integer blogId = (Integer) request.get("blogId");

        //验证身份
        if (userService.isPasswordRight(username, password)) {
            microBlogService.favorMicroBlog(blogId);
            favorService.createItem(blogId, username);
            return new CommonResponseForm(0, "Favor MicroBlog Successfully!", null);
        }
        else
            return new CommonResponseForm(1, "Illegal Request!", null);
    }

    //转发微博
    @PostMapping(value = "/relayMicroBlog")
    public CommonResponseForm relayMicroBlog(@RequestBody Map<String, Object> request) {
        String username = (String) request.get("username");
        String password = (String) request.get("password");
        Integer blogId = (Integer) request.get("blogId");

        //验证身份
        if (userService.isPasswordRight(username, password)) {
            microBlogService.relayMicroBlog(username, blogId);
            return new CommonResponseForm(0, "Replay MicroBlog Successfully!", null);
        }
        else
            return new CommonResponseForm(1, "Illegal Request!", null);
    }
}
