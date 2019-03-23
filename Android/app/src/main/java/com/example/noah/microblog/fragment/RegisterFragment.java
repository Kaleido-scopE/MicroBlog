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

public class RegisterFragment extends Fragment {
    private EditText mUsernameEdit;
    private EditText mPasswordEdit;
    private EditText mNicknameEdit;
    private Button mRegisterButton;
    private RegisterListener mRegisterListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);

        mUsernameEdit = v.findViewById(R.id.username_edit);
        mPasswordEdit = v.findViewById(R.id.password_edit);
        mNicknameEdit = v.findViewById(R.id.nickname_edit);
        mRegisterButton = v.findViewById(R.id.register_button);

        mPasswordEdit.setTypeface(mUsernameEdit.getTypeface());

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsernameEdit.getText().toString();
                String password = mPasswordEdit.getText().toString();
                String nickname = mNicknameEdit.getText().toString();

                HttpUtil.register(username, password, nickname, new CallbackAdapter(getActivity()) {
                    @Override
                    public void onParseResponseForm(ResponseForm response) {
                        mRegisterListener.onRegisterButtonClick(response.getStatus(), response.getMessage());
                    }
                });
            }
        });

        return v;
    }

    //工厂方法
    public static RegisterFragment newInstance(RegisterListener listener) {
        RegisterFragment fragment = new RegisterFragment();
        fragment.setRegisterButton(listener);
        return fragment;
    }

    public interface RegisterListener {
        void onRegisterButtonClick(Integer status, String message);
    }
    private void setRegisterButton(RegisterListener listener) {
        mRegisterListener = listener;
    }
}
