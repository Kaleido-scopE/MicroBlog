package com.example.noah.microblog.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.*;

import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.example.noah.microblog.R;
import com.example.noah.microblog.component.CommentAdapter;
import com.example.noah.microblog.entity.CommentEntity;
import com.example.noah.microblog.entity.MicroBlogEntity;
import com.example.noah.microblog.form.ResponseForm;
import com.example.noah.microblog.utils.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BlogDetailFragment extends Fragment {
    private ImageView mUserAvatar;
    private TextView mUserTitle;
    private TextView mBlogContent;
    private ImageView mBlogContentPic;
    private TextView mCommentCount;
    private TextView mFavorIcon;
    private TextView mCommentIcon;
    private TextView mTransIcon;
    private LinearLayout mFavorListWrapper;
    private TextView mFavorList;
    private RecyclerView mCommentList;
    private LinearLayout mCommentEditWrapper;
    private EditText mCommentEdit;
    private Button mCommentSubmit;

    private MicroBlogEntity microBlogEntity;
    private final String initialFavorListText = "❤ ";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_blog_detail, container, false);

        mUserAvatar = v.findViewById(R.id.user_avatar);
        mUserTitle = v.findViewById(R.id.user_title);
        mBlogContent = v.findViewById(R.id.blog_content);
        mBlogContentPic = v.findViewById(R.id.blog_content_pic);
        mCommentCount = v.findViewById(R.id.comment_cnt);
        mFavorIcon = v.findViewById(R.id.favor_blog);
        mCommentIcon = v.findViewById(R.id.comment_blog);
        mTransIcon = v.findViewById(R.id.trans_blog);
        mFavorListWrapper = v.findViewById(R.id.favor_list_wrapper);
        mFavorList = v.findViewById(R.id.favor_list);
        mCommentList = v.findViewById(R.id.comment_recycler_view);
        mCommentEditWrapper = v.findViewById(R.id.comment_edit_wrapper);
        mCommentEdit = v.findViewById(R.id.comment_edit);
        mCommentSubmit = v.findViewById(R.id.comment_submit);

        //设置图片区的显示自适应
        BitmapUtil.setAdjustImageView(getActivity(), mBlogContentPic, 0);

        //Add按钮点击事件
        mCommentIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCommentEditWrapper.getVisibility() == View.GONE)
                    mCommentEditWrapper.setVisibility(View.VISIBLE);
                else
                    mCommentEditWrapper.setVisibility(View.GONE);
            }
        });

        //转发Icon监听
        mTransIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //登录后才能转发
                if (StatusManager.getIsLogin())
                    HttpUtil.relayMicroBlog(StatusManager.getUsername(), StatusManager.getPassword(),
                            microBlogEntity.getId(), new CallbackAdapter(getActivity()) {
                                @Override
                                public void onParseResponseForm(ResponseForm response) {
                                    Toast.makeText(getActivity(), response.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                else
                    Toast.makeText(getActivity(), "You're not logged in yet！", Toast.LENGTH_LONG).show();
            }
        });

        //Submit按钮点击事件
        mCommentSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //登录后才能提交评论
                if (StatusManager.getIsLogin())
                    HttpUtil.postComment(StatusManager.getUsername(), StatusManager.getPassword(),
                            mCommentEdit.getText().toString(), microBlogEntity.getId(), new CallbackAdapter(getActivity()) {
                        @Override
                        public void onParseResponseForm(ResponseForm response) {
                            Toast.makeText(getActivity(), response.getMessage(), Toast.LENGTH_LONG).show();

                            //取消EditCom的焦点，并置内容为空
                            mCommentEdit.setText("");
                            mCommentEdit.clearFocus();
                            InputMethodManager manager = (InputMethodManager) mCommentEdit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            manager.hideSoftInputFromWindow(mCommentEdit.getWindowToken(), 0);
                            mCommentIcon.callOnClick();

                            //刷新评论列表
                            refreshComment();
                        }
                    });
                else
                    Toast.makeText(getActivity(), "You're not logged in yet！", Toast.LENGTH_LONG).show();
            }
        });

        String username = StatusManager.getIsLogin() ? StatusManager.getUsername() : "";
        Integer blogId = getArguments().getInt("blogId");
        HttpUtil.getBlogById(username, blogId, new CallbackAdapter(getActivity()) {
            @Override
            public void onParseResponseForm(ResponseForm response) {
                JSONObject object = (JSONObject) response.getData();

                try {
                    JSONArray favorUsers = object.getJSONArray("favorUsers");
                    List<String> usersList = new ArrayList<>();
                    for (int i = 0; i < favorUsers.length(); i++)
                        usersList.add(favorUsers.getString(i));

                    microBlogEntity = new MicroBlogEntity(object.getInt("id"), object.getString("username"), object.getString("nickname"),
                            object.getString("content"), object.getString("contentPic"), object.getInt("favorCount"), usersList,
                            object.getBoolean("clickable"), object.getLong("publishTime"));

                    loadBlogDetail();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_blog_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                createShareContent();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createShareContent() {
        //登录后才能分享
        if (StatusManager.getIsLogin()) {
            String content =
                      "----- Moment -----" + "\n"
                    + microBlogEntity.getContent() + "\n"
                    + "From: " + microBlogEntity.getNickname() + "\n"
                    + "----- Moment -----";

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, content);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }
        else
            Toast.makeText(getActivity(), "You're not logged in yet！", Toast.LENGTH_LONG).show();
    }

    private void refreshComment() {
        CommentAdapter.loadCommentList(getActivity(), mCommentList, microBlogEntity.getId(), new CommentAdapter.CommentAdapterListener() {
            @Override
            public void onViewRender(List<CommentEntity> entities) {
                String countText = "Comment:" + entities.size();
                mCommentCount.setText(countText);
            }
        });
    }

    private void loadBlogDetail() {
        //设置头像
        BitmapUtil.setAvatarTo(getActivity(), microBlogEntity.getUsername(), mUserAvatar);

        //设置昵称
        String userTitleText = microBlogEntity.getNickname() + "\n" + TimeUtil.hmFromDate(new Date(microBlogEntity.getPublishTime()));
        mUserTitle.setText(userTitleText);

        //设置微博内容
        mBlogContent.setText(microBlogEntity.getContent());

        //设置微博图片
        if (microBlogEntity.getContentPic().length() > 0) {
            Bitmap bitmap = BitmapUtil.base64ToBitmap(microBlogEntity.getContentPic());
            mBlogContentPic.setImageBitmap(bitmap);
            mBlogContentPic.setVisibility(View.VISIBLE);
        }

        //刷新评论列表
        refreshComment();

        //设置点赞数
        String favorCount = " " + microBlogEntity.getFavorCount();
        mFavorIcon.setText(favorCount);

        //点赞列表，点赞用户不为空时才显示
        if (!microBlogEntity.getFavorUsers().isEmpty()) {
            mFavorListWrapper.setVisibility(View.VISIBLE);

            StringBuilder builder = new StringBuilder(mFavorList.getText().toString());
            for (int i = 0; i < microBlogEntity.getFavorUsers().size(); i++) {
                builder.append(microBlogEntity.getFavorUsers().get(i));
                if (i != microBlogEntity.getFavorUsers().size() - 1)
                    builder.append(", ");
            }

            mFavorList.setText(builder.toString());
        }

        //判断此时Icon是否可点击
        if (!microBlogEntity.getClickable())
            mFavorIcon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favor_after_black_24dp, 0, 0, 0);
        else
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
                        microBlogEntity.getFavorUsers().add(StatusManager.getNickname());
                        mFavorListWrapper.setVisibility(View.VISIBLE);

                        //设置不可再点击
                        mFavorIcon.setOnClickListener(null);
                        microBlogEntity.setClickable(false);
                        microBlogEntity.setFavorCount(microBlogEntity.getFavorCount() + 1);

                        //提交请求
                        HttpUtil.favorMicroBlog(StatusManager.getUsername(), StatusManager.getPassword(),
                                microBlogEntity.getId(), new CallbackAdapter(getActivity()) {
                                    @Override
                                    public void onParseResponseForm(ResponseForm response) {

                                    }
                                });
                    }
                    else
                        Toast.makeText(getActivity(), "You're not logged in yet！", Toast.LENGTH_LONG).show();
                }
            });
    }

    //工厂方法
    public static BlogDetailFragment newInstance(Integer blogId) {
        BlogDetailFragment fragment = new BlogDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("blogId", blogId);
        fragment.setArguments(bundle);
        return fragment;
    }
}
