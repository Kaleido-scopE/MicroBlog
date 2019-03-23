package com.example.noah.microblog.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.noah.microblog.R;
import com.example.noah.microblog.fragment.BlogDetailFragment;

public class BlogDetailActivity extends AppCompatActivity {
    public static final int requestCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);

        Integer blogId = getIntent().getIntExtra("blogId", 1);

        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_container, BlogDetailFragment.newInstance(blogId)).
                commit();
    }
}
