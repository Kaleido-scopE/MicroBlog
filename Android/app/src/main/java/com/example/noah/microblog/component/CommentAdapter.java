package com.example.noah.microblog.component;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.noah.microblog.R;
import com.example.noah.microblog.entity.CommentEntity;
import com.example.noah.microblog.form.ResponseForm;
import com.example.noah.microblog.utils.CallbackAdapter;
import com.example.noah.microblog.utils.HttpUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//单条评论记录
class CommentHolder extends RecyclerView.ViewHolder {
    private TextView mUserNickname;
    private TextView mCommentContent;

    private CommentEntity commentItem;

    CommentHolder(LayoutInflater inflater, ViewGroup viewGroup) {
        super(inflater.inflate(R.layout.comment_item, viewGroup, false));

        mUserNickname = itemView.findViewById(R.id.user_nickname);
        mCommentContent = itemView.findViewById(R.id.comment_content);
    }

    void bind(CommentEntity entity) {
        commentItem = entity;
        String nickname = entity.getNickname() + ": ";
        String content = entity.getContent();
        mUserNickname.setText(nickname);
        mCommentContent.setText(content);
    }
}

//评论列表Adapter
public class CommentAdapter extends RecyclerView.Adapter<CommentHolder> {
    private Activity activity;
    private List<CommentEntity> mCommentList;

    public CommentAdapter(Activity activity, List<CommentEntity> mCommentList) {
        this.activity = activity;
        this.mCommentList = mCommentList;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        return new CommentHolder(layoutInflater, viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder commentHolder, int i) {
        CommentEntity comment = mCommentList.get(i);
        commentHolder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return mCommentList.size();
    }

    //加载评论列表
    public static void loadCommentList(final Activity activity, final RecyclerView recyclerView, final Integer blogId, final CommentAdapterListener listener) {
        HttpUtil.getBlogComment(blogId, new CallbackAdapter(activity) {
            @Override
            public void onParseResponseForm(ResponseForm response) {
                List<CommentEntity> entities = new ArrayList<>();
                JSONArray data = (JSONArray) response.getData();
                JSONObject object;

                try {
                    for (int i = 0; i < data.length(); i++) {
                        object = data.getJSONObject(i);
                        entities.add(new CommentEntity(object.getString("username"), object.getString("nickname"), object.getString("content"),
                                object.getLong("publishTime"), object.getInt("blogId")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                recyclerView.setAdapter(new CommentAdapter(activity, entities));
                listener.onViewRender(entities);
            }
        });
    }

    public interface CommentAdapterListener {
        void onViewRender(List<CommentEntity> entities);
    }
}
