package com.example.noah.microblog.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.example.noah.microblog.R;
import com.example.noah.microblog.form.ResponseForm;
import com.example.noah.microblog.utils.BitmapUtil;
import com.example.noah.microblog.utils.CallbackAdapter;
import com.example.noah.microblog.utils.HttpUtil;
import com.example.noah.microblog.utils.StatusManager;

import java.io.InputStream;

public class BlogPostFragment extends Fragment {
    private static final int CHOOSE_PIC = 2;

    private EditText mBlogContent;
    private ImageView mMomentAdd;
    private Button mBlogSubmit;

    private static String momentBase64 = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_blog_post, container, false);

        mBlogContent = v.findViewById(R.id.blog_content);
        mMomentAdd = v.findViewById(R.id.moment_add);
        mBlogSubmit = v.findViewById(R.id.blog_submit);

        mMomentAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, CHOOSE_PIC);
            }
        });

        //为提交按钮设置点击事件
        mBlogSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpUtil.postMicroBlog(StatusManager.getUsername(), StatusManager.getPassword(),
                        mBlogContent.getText().toString(), momentBase64, new CallbackAdapter(getActivity()) {
                    @Override
                    public void onParseResponseForm(ResponseForm response) {
                        getActivity().finish();
                    }
                });
            }
        });

        return v;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHOOSE_PIC && resultCode == Activity.RESULT_OK) {
            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(data.getData());
                Bitmap contentPic = BitmapFactory.decodeStream(inputStream);
                momentBase64 = BitmapUtil.bitmapToBase64(contentPic);
                mMomentAdd.setImageBitmap(contentPic);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //工厂方法
    public static BlogPostFragment newInstance() {
        BlogPostFragment fragment = new BlogPostFragment();
        return fragment;
    }

}
