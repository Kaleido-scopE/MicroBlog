package com.example.noah.microblog.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.example.noah.microblog.R;
import com.example.noah.microblog.fragment.RegisterFragment;

public class RegisterActivity extends AppCompatActivity {
    public static final int requestCode = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);

        RegisterFragment registerFragment = RegisterFragment.newInstance(new RegisterFragment.RegisterListener() {
            @Override
            public void onRegisterButtonClick(final Integer status, final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
                        if (status == 0)
                            finish();
                    }
                });
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, registerFragment).commit();
    }
}
