package com.example.noah.microblog.component;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.example.noah.microblog.R;
import com.example.noah.microblog.activity.BlogDetailActivity;
import com.example.noah.microblog.entity.CommentEntity;
import com.example.noah.microblog.entity.MicroBlogEntity;
import com.example.noah.microblog.form.ResponseForm;
import com.example.noah.microblog.utils.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

//单条微博记录
class BlogHolder extends RecyclerView.ViewHolder {
    private ImageView mUserAvatar;
    private TextView mUserNickname;
    private TextView mBlogContent;
    private ImageView mBlogContentPic;
    private TextView mPublishTime;
    private TextView mFavorIcon;
    private TextView mCommentIcon;
    private TextView mTransIcon;
    private LinearLayout mFavorListWrapper;
    private TextView mFavorList;
    private RecyclerView mCommentList;
    private LinearLayout mCommentEditWrapper;
    private EditText mCommentEdit;
    private Button mCommentSubmit;

    private final String initialFavorListText = "❤ ";

    BlogHolder(LayoutInflater inflater, final ViewGroup viewGroup) {
        super(inflater.inflate(R.layout.blog_item, viewGroup, false));

        mUserAvatar = itemView.findViewById(R.id.user_avatar);
        mUserNickname = itemView.findViewById(R.id.user_nickname);
        mBlogContent = itemView.findViewById(R.id.blog_content);
        mBlogContentPic = itemView.findViewById(R.id.blog_content_pic);
        mPublishTime = itemView.findViewById(R.id.blog_pub_time);
        mFavorIcon = itemView.findViewById(R.id.favor_blog);
        mCommentIcon = itemView.findViewById(R.id.comment_blog);
        mTransIcon = itemView.findViewById(R.id.trans_blog);
        mFavorListWrapper = itemView.findViewById(R.id.favor_list_wrapper);
        mFavorList = itemView.findViewById(R.id.favor_list);
        mCommentList = itemView.findViewById(R.id.comment_recycler_view);
        mCommentEditWrapper = itemView.findViewById(R.id.comment_edit_wrapper);
        mCommentEdit = itemView.findViewById(R.id.comment_edit);
        mCommentSubmit = itemView.findViewById(R.id.comment_submit);
    }

    //设定每一项的显示效果
    void bind(final Activity activity, final MicroBlogEntity entity, final RecyclerView parent) {
        //设置图片区的显示自适应
        BitmapUtil.setAdjustImageView(activity, mBlogContentPic, 55);

        //设置头像
        BitmapUtil.setAvatarTo(activity, entity.getUsername(), mUserAvatar);

        //设置昵称
        mUserNickname.setText(entity.getNickname());

        //设置微博内容
        mBlogContent.setText(entity.getContent());

        //设置微博图片，长度大于0时才设置
        if (entity.getContentPic().length() > 0) {
            Bitmap bitmap = BitmapUtil.base64ToBitmap(entity.getContentPic());
            mBlogContentPic.setImageBitmap(bitmap);
            mBlogContentPic.setVisibility(View.VISIBLE);
        }

        //设置发布时间
        mPublishTime.setText(TimeUtil.hmFromDate(new Date(entity.getPublishTime())));

        //点赞数
        String favorCount = " " + entity.getFavorCount();
        mFavorIcon.setText(favorCount);

        //点赞列表，点赞用户不为空时才显示
        if (!entity.getFavorUsers().isEmpty()) {
            mFavorListWrapper.setVisibility(View.VISIBLE);

            StringBuilder builder = new StringBuilder(mFavorList.getText().toString());
            for (int i = 0; i < entity.getFavorUsers().size(); i++) {
                builder.append(entity.getFavorUsers().get(i));
                if (i != entity.getFavorUsers().size() - 1)
                    builder.append(", ");
            }

            mFavorList.setText(builder.toString());
        }

        //判断此时Icon是否可点击
        if (!entity.getClickable())
            mFavorIcon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favor_after_black_24dp, 0, 0, 0);
        else //设置监听器
            mFavorIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //登录后才能点赞
                    if (StatusManager.getIsLogin()) {
                        String favorCountText = mFavorIcon.getText().toString();
                        Integer curFavorCount = Integer.valueOf(favorCountText.substring(1));

                        //更改图标及点赞数
                        String newText = " " + (curFavorCount + 1);
                        mFavorIcon.setText(newText);
                        mFavorIcon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favor_after_black_24dp, 0, 0, 0);

                        //更改点赞用户列表
                        String curList = mFavorList.getText().toString();
                        String newList;

                        if (curList.equals(initialFavorListText))
                            newList = curList + StatusManager.getNickname();
                        else
                            newList = curList + ", " + StatusManager.getNickname();

                        mFavorList.setText(newList);
                        entity.getFavorUsers().add(StatusManager.getNickname());
                        mFavorListWrapper.setVisibility(View.VISIBLE);

                        //设置不可再点击
                        mFavorIcon.setOnClickListener(null);
                        entity.setClickable(false);
                        entity.setFavorCount(entity.getFavorCount() + 1);

                        //提交请求
                        HttpUtil.favorMicroBlog(StatusManager.getUsername(), StatusManager.getPassword(),
                                entity.getId(), new CallbackAdapter(activity) {
                                    @Override
                                    public void onParseResponseForm(ResponseForm response) {

                                    }
                                });
                    }
                    else
                        Toast.makeText(activity, "You're not logged in yet！", Toast.LENGTH_LONG).show();
                }
            });


        //修改Wrapper的显示状态
        mCommentIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //登录后才能显示评论框
                if (StatusManager.getIsLogin()) {
                    if (mCommentEditWrapper.getVisibility() == View.GONE)
                        mCommentEditWrapper.setVisibility(View.VISIBLE);
                    else
                        mCommentEditWrapper.setVisibility(View.GONE);
                }
                else
                    Toast.makeText(activity, "You're not logged in yet！", Toast.LENGTH_LONG).show();
            }
        });

        //查看详情
        mBlogContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //查看详情
                Intent intent = new Intent(activity, BlogDetailActivity.class);
                intent.putExtra("blogId", entity.getId());
                activity.startActivityForResult(intent, BlogDetailActivity.requestCode);
            }
        });

        //转发Icon监听
        mTransIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //登录后才能转发
                if (StatusManager.getIsLogin())
                    HttpUtil.relayMicroBlog(StatusManager.getUsername(), StatusManager.getPassword(),
                            entity.getId(), new CallbackAdapter(activity) {
                        @Override
                        public void onParseResponseForm(ResponseForm response) {
                            Toast.makeText(activity, response.getMessage(), Toast.LENGTH_LONG).show();

                            //转发后刷新
                            BlogAdapter.loadBlogList(activity, parent);
                        }
                    });
                else
                    Toast.makeText(activity, "You're not logged in yet！", Toast.LENGTH_LONG).show();
            }
        });

        //提交评论按钮监听
        mCommentSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //登录后才能提交评论
                if (StatusManager.getIsLogin())
                    HttpUtil.postComment(StatusManager.getUsername(), StatusManager.getPassword(),
                            mCommentEdit.getText().toString(), entity.getId(), new CallbackAdapter(activity) {
                        @Override
                        public void onParseResponseForm(ResponseForm response) {
                            Toast.makeText(activity, response.getMessage(), Toast.LENGTH_LONG).show();

                            //取消EditCom的焦点，并置内容为空
                            mCommentEdit.setText("");
                            mCommentEdit.clearFocus();
                            InputMethodManager manager = (InputMethodManager) mCommentEdit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            manager.hideSoftInputFromWindow(mCommentEdit.getWindowToken(), 0);
                            mCommentIcon.callOnClick();

                            //刷新评论列表
                            CommentAdapter.loadCommentList(activity, mCommentList, entity.getId(), new CommentAdapter.CommentAdapterListener() {
                                @Override
                                public void onViewRender(List<CommentEntity> entities) {

                                }
                            });
                        }
                    });
                else
                    Toast.makeText(activity, "You're not logged in yet！", Toast.LENGTH_LONG).show();
            }
        });

        //加载评论列表
        CommentAdapter.loadCommentList(activity, mCommentList, entity.getId(), new CommentAdapter.CommentAdapterListener() {
            @Override
            public void onViewRender(List<CommentEntity> entities) {

            }
        });
    }
}

//微博列表Adapter
public class BlogAdapter extends RecyclerView.Adapter<BlogHolder> {
    private Activity activity;
    private List<MicroBlogEntity> mMircoBlogList;
    private RecyclerView parent;

    public BlogAdapter(Activity activity, List<MicroBlogEntity> mMircoBlogList) {
        this.activity = activity;
        this.mMircoBlogList = mMircoBlogList;
    }

    @NonNull
    @Override
    public BlogHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        return new BlogHolder(layoutInflater, viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogHolder blogHolder, int i) {
        MicroBlogEntity blog = mMircoBlogList.get(i);
        blogHolder.bind(activity, blog, parent);
    }

    @Override
    public int getItemCount() {
        return mMircoBlogList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        parent = recyclerView;
    }

    //加载微博列表
    public static void loadBlogList(final Activity activity, final RecyclerView recyclerView) {
        String username = StatusManager.getIsLogin() ? StatusManager.getUsername() : "";

        HttpUtil.getBlogList(username, new CallbackAdapter(activity) {
            @Override
            public void onParseResponseForm(ResponseForm response) {
                List<MicroBlogEntity> entities = new ArrayList<>();
                JSONArray data = (JSONArray) response.getData();
                JSONObject object;
                List<String> usersList;

                try {
                    for (int i = 0; i < data.length(); i++) {
                        object = data.getJSONObject(i);
                        JSONArray favorUsers = object.getJSONArray("favorUsers");
                        usersList = new ArrayList<>();
                        for (int j = 0; j < favorUsers.length(); j++)
                            usersList.add(favorUsers.getString(j));
                        entities.add(new MicroBlogEntity(object.getInt("id"), object.getString("username"), object.getString("nickname"),
                                object.getString("content"), object.getString("contentPic"), object.getInt("favorCount"), usersList,
                                object.getBoolean("clickable"), object.getLong("publishTime")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                recyclerView.setAdapter(new BlogAdapter(activity, entities));
            }
        });
    }
}
