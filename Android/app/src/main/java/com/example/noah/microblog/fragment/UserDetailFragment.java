package com.example.noah.microblog.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;
import com.example.noah.microblog.R;
import com.example.noah.microblog.form.ResponseForm;
import com.example.noah.microblog.utils.BitmapUtil;
import com.example.noah.microblog.utils.CallbackAdapter;
import com.example.noah.microblog.utils.HttpUtil;
import com.example.noah.microblog.utils.StatusManager;

import java.io.InputStream;

public class UserDetailFragment extends Fragment {
    private static final int CHOOSE_AVATAR = 1;

    private LinearLayout mAvatarSet;
    private LinearLayout mNicknameSet;
    private LinearLayout mPasswordSet;
    private ImageView mUserAvatar;
    private TextView mUserNickname;
    private Button mLogoutButton;
    private UserDetailListener mUserDetailListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_detail, container, false);

        mAvatarSet = v.findViewById(R.id.avatar_set);
        mNicknameSet = v.findViewById(R.id.nickname_set);
        mPasswordSet = v.findViewById(R.id.password_set);
        mUserAvatar = v.findViewById(R.id.user_avatar);
        mUserNickname = v.findViewById(R.id.user_nickname);
        mLogoutButton = v.findViewById(R.id.logout_button);

        //修改头像
        mAvatarSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, CHOOSE_AVATAR);
            }
        });

        //修改昵称
        mNicknameSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog(0);
            }
        });

        //修改密码
        mPasswordSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog(1);
            }
        });

        //注销
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserDetailListener.onLogoutButtonClick();
            }
        });

        loadUserDetail();

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHOOSE_AVATAR && resultCode == Activity.RESULT_OK)
            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(data.getData());
                final Bitmap newAvatar = BitmapFactory.decodeStream(inputStream);
                //发送头像到数据库
                String base64Avatar = BitmapUtil.bitmapToBase64(newAvatar);

                HttpUtil.setUserAvatar(StatusManager.getUsername(), StatusManager.getPassword(), base64Avatar, new CallbackAdapter(getActivity()) {
                    @Override
                    public void onParseResponseForm(ResponseForm response) {
                        Toast.makeText(getActivity(), response.getMessage(), Toast.LENGTH_LONG).show();

                        //返回成功才修改头像显示
                        if (response.getStatus() == 0)
                            mUserAvatar.setImageBitmap(newAvatar);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    private void loadUserDetail() {
        //请求头像
        BitmapUtil.setAvatarTo(getActivity(), StatusManager.getUsername(), mUserAvatar);

        //请求昵称
        HttpUtil.getUserNickname(StatusManager.getUsername(), new CallbackAdapter(getActivity()) {
            @Override
            public void onParseResponseForm(ResponseForm response) {
                mUserNickname.setText((String) response.getData());
            }
        });
    }

    //显示指定类型的输入对话框，0：普通；1：密码
    private void showInputDialog(final Integer code) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        final View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_input, null);

        dialog.setTitle(code == 0 ? "输入新昵称" : "输入新密码");
        dialog.setView(dialogView);

        final EditText mContentInput = dialogView.findViewById(R.id.content_input);
        if (code == 0)
            mContentInput.setInputType(InputType.TYPE_CLASS_TEXT);
        else {
            mContentInput.setTypeface(Typeface.DEFAULT);
            mContentInput.setTransformationMethod(new PasswordTransformationMethod());
        }
        //确定选项
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String contentInput = mContentInput.getText().toString();
                if (code == 0)
                    HttpUtil.setUserNickname(StatusManager.getUsername(), StatusManager.getPassword(), contentInput, new CallbackAdapter(getActivity()) {
                        @Override
                        public void onParseResponseForm(ResponseForm response) {
                            String message = response.getMessage();
                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                            //更换昵称
                            mUserNickname.setText(contentInput);
                        }
                    });
                else
                    HttpUtil.modifyPassword(StatusManager.getUsername(), StatusManager.getPassword(), contentInput, new CallbackAdapter(getActivity()) {
                        @Override
                        public void onParseResponseForm(ResponseForm response) {
                            String message = response.getMessage();
                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                            //修改密码后注销页面
                            mUserDetailListener.onLogoutButtonClick();
                        }
                    });
            }
        });

        //取消选项
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialog.show();
    }

    //工厂方法
    public static UserDetailFragment newInstance(UserDetailListener listener) {
        UserDetailFragment fragment = new UserDetailFragment();
        fragment.setUserDetailListener(listener);
        return fragment;
    }

    //提供给Activity的接口
    public interface UserDetailListener {
        void onLogoutButtonClick();
    }
    private void setUserDetailListener(UserDetailListener listener) {
        mUserDetailListener = listener;
    }
}