package com.example.noah.microblog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;
import com.example.noah.microblog.R;
import com.example.noah.microblog.fragment.BlogListFragment;
import com.example.noah.microblog.fragment.LoginFragment;
import com.example.noah.microblog.fragment.UserDetailFragment;
import com.example.noah.microblog.utils.StatusManager;

public class MainActivity extends AppCompatActivity {
    public static final int requestCode = 3;
    private static final String TAG = "com.example.noah.microblog.activity.MainActivity";

    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //启动时置为未登录状态，并加载微博列表
        StatusManager.setIsLogin(false);
        launchBlogListFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BlogDetailActivity.requestCode || requestCode == BlogPostActivity.requestCode)
            navigation.setSelectedItemId(R.id.navigation_home);
    }

    //设置底部导航栏
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    launchBlogListFragment();
                    return true;
                case R.id.navigation_plus:
                    launchBlogPostActivity();
                    return true;
                case R.id.navigation_me:
                    if (StatusManager.getIsLogin())//已登录则跳转详情页面
                        launchUserDetailFragment();
                    else//否则跳转登录页面
                        launchLoginFragment();
                    return true;
            }
            return false;
        }
    };

    //启动BlogListFragment
    private void launchBlogListFragment() {
        BlogListFragment blogListFragment = BlogListFragment.newInstance();
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_container, blogListFragment).
                commitAllowingStateLoss();
    }

    //启动LoginFragment
    private void launchLoginFragment() {
        //为LoginFragment中的按钮设置回调
        LoginFragment loginFragment =
                LoginFragment.newInstance(new LoginFragment.LoginListener() {
                    @Override
                    public void onLoginButtonClick(Integer status, final String message) {
                        StatusManager.setIsLogin(status == 0);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //如果登录成功跳转用户详情页面
                                if (StatusManager.getIsLogin())
                                    launchUserDetailFragment();
                                else
                                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                            }
                        });

                    }

                    @Override
                    public void onRegisterButtonClick() {
                        //点击注册按钮跳转注册页面
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                launchRegisterActivity();
                            }
                        });
                    }
                });

        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_container, loginFragment).
                commit();
    }

    //启动UserDetailFragment
    private void launchUserDetailFragment() {
        UserDetailFragment userDetailFragment =
                UserDetailFragment.newInstance(new UserDetailFragment.UserDetailListener() {
                    @Override
                    public void onLogoutButtonClick() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                StatusManager.setIsLogin(false);
                                launchLoginFragment();
                            }
                        });
                    }
                });

        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_container, userDetailFragment).
                commit();
    }

    //启动承载RegisterFragment的RegisterActivity
    private void launchRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    //启动承载BlogPostFragment的BlogPostActivity
    private void launchBlogPostActivity() {
        //登录后才能发布微博
        if (StatusManager.getIsLogin())
            startActivityForResult(new Intent(this, BlogPostActivity.class), BlogPostActivity.requestCode);
        else
            Toast.makeText(this, "You're not logged in yet！", Toast.LENGTH_LONG).show();
    }
}
