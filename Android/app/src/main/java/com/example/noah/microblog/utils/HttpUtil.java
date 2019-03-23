package com.example.noah.microblog.utils;

import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

public class HttpUtil {
    private static final String TAG = "com.example.noah.microblog.utils.HttpUtils";
    private static final String endPoint = "http://192.168.1.100:8080";
//    private static final String endPoint = "http://10.0.2.2:8080";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final OkHttpClient client = new OkHttpClient();

    private static void post(String targetUrl, String JSONStr, CallbackAdapter adapter) {
        RequestBody body = RequestBody.create(JSON, JSONStr);
        Request request = new Request.Builder().url(endPoint + targetUrl).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(adapter);
    }

    //登录
    public static void login(String username, String password, CallbackAdapter adapter) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", username);
            jsonObject.put("password", password);

            post("/login", jsonObject.toString(), adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //注册
    public static void register(String username, String password, String nickname, CallbackAdapter adapter) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", username);
            jsonObject.put("password", password);
            jsonObject.put("nickname", nickname);

            post("/register", jsonObject.toString(), adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //修改密码
    public static void modifyPassword(String username, String password, String newPassword, CallbackAdapter adapter) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", username);
            jsonObject.put("password", password);
            jsonObject.put("newPassword", newPassword);

            post("/modifyPassword", jsonObject.toString(), adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //设置头像
    public static void setUserAvatar(String username, String password, String base64Avatar, CallbackAdapter adapter) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", username);
            jsonObject.put("password", password);
            jsonObject.put("base64Avatar", base64Avatar);

            post("/setUserAvatar", jsonObject.toString(), adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //获得头像
    public static void getUserAvatar(String username, CallbackAdapter adapter) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", username);

            post("/getUserAvatar", jsonObject.toString(), adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //设置昵称
    public static void setUserNickname(String username, String password, String newNickname, CallbackAdapter adapter) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", username);
            jsonObject.put("password", password);
            jsonObject.put("newNickname", newNickname);

            post("/setUserNickname", jsonObject.toString(), adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //获得昵称
    public static void getUserNickname(String username, CallbackAdapter adapter) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", username);

            post("/getUserNickname", jsonObject.toString(), adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //============================WEIBO============================//

    //获得指定Id微博的详情
    public static void getBlogById(String username, Integer blogId, CallbackAdapter adapter) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", username);
            jsonObject.put("blogId", blogId);

            post("/getBlogById", jsonObject.toString(), adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //获取微博列表
    public static void getBlogList(String username, CallbackAdapter adapter) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", username);

            post("/getBlogList", jsonObject.toString(), adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //获得指定微博的评论列表
    public static void getBlogComment(Integer blogId, CallbackAdapter adapter) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("blogId", blogId);

            post("/getBlogComment", jsonObject.toString(), adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //发表微博
    public static void postMicroBlog(String username, String password, String content, String contentPic, CallbackAdapter adapter) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", username);
            jsonObject.put("password", password);
            jsonObject.put("content", content);
            jsonObject.put("contentPic", contentPic);

            post("/postMicroBlog", jsonObject.toString(), adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //发表评论
    public static void postComment(String username, String password, String content, Integer blogId, CallbackAdapter adapter) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", username);
            jsonObject.put("password", password);
            jsonObject.put("content", content);
            jsonObject.put("blogId", blogId);

            post("/postComment", jsonObject.toString(), adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //点赞微博
    public static void favorMicroBlog(String username, String password, Integer blogId, CallbackAdapter adapter) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", username);
            jsonObject.put("password", password);
            jsonObject.put("blogId", blogId);

            post("/favorMicroBlog", jsonObject.toString(), adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //转发微博
    public static void relayMicroBlog(String username, String password, Integer blogId, CallbackAdapter adapter) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", username);
            jsonObject.put("password", password);
            jsonObject.put("blogId", blogId);

            post("/relayMicroBlog", jsonObject.toString(), adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
