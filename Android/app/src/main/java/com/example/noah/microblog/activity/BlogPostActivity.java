package com.example.noah.microblog.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.noah.microblog.R;
import com.example.noah.microblog.fragment.BlogPostFragment;

public class BlogPostActivity extends AppCompatActivity {
    public static final int requestCode = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);

        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_container, BlogPostFragment.newInstance()).
                commit();
    }
}
