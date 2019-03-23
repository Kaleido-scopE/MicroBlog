package com.example.noah.microblog.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import com.example.noah.microblog.R;
import com.example.noah.microblog.form.ResponseForm;
import com.example.noah.microblog.utils.CallbackAdapter;
import com.example.noah.microblog.utils.HttpUtil;
import com.example.noah.microblog.utils.StatusManager;

public class LoginFragment extends Fragment {
    private EditText mUsernameEdit;
    private EditText mPasswordEdit;
    private Button mLoginButton;
    private Button mRegisterButton;
    private LoginListener mLoginListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        mUsernameEdit = v.findViewById(R.id.username_edit);
        mPasswordEdit = v.findViewById(R.id.password_edit);
        mLoginButton = v.findViewById(R.id.login_button);
        mRegisterButton = v.findViewById(R.id.register_button);

        mPasswordEdit.setTypeface(mUsernameEdit.getTypeface());

        //为登录按钮设置监听
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String usernameInput = mUsernameEdit.getText().toString();
                final String passwordInput = mPasswordEdit.getText().toString();

                HttpUtil.login(usernameInput, passwordInput, new CallbackAdapter(getActivity()) {
                    @Override
                    public void onParseResponseForm(ResponseForm response) {
                        //根据登录是否成功确定是否要在StatusManager中设置用户
                        if (response.getStatus() == 0) {
                            StatusManager.setUsername(usernameInput);
                            StatusManager.setPassword(passwordInput);
                            StatusManager.setNickname((String) response.getData());
                        }
                        //返回状态码，UI渲染交付Activity处理
                        mLoginListener.onLoginButtonClick(response.getStatus(), response.getMessage());
                    }
                });
            }
        });

        //为注册按钮设置监听
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginListener.onRegisterButtonClick();
            }
        });

        return v;
    }

    //工厂方法
    public static LoginFragment newInstance(LoginListener listener) {
        LoginFragment fragment = new LoginFragment();
        fragment.setLoginListener(listener);
        return fragment;
    }

    //提供给Activity的接口
    public interface LoginListener {
        void onLoginButtonClick(Integer status, String message);
        void onRegisterButtonClick();
    }
    private void setLoginListener(LoginListener listener) {
        mLoginListener = listener;
    }
}
