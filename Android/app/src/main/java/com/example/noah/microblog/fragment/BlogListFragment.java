package com.example.noah.microblog.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.noah.microblog.R;
import com.example.noah.microblog.component.BlogAdapter;

public class BlogListFragment extends Fragment {
    private RecyclerView mBlogList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_blog_list, container, false);

        mBlogList = v.findViewById(R.id.blog_recycler_view);
        BlogAdapter.loadBlogList(getActivity(), mBlogList);//加载微博列表

        return v;
    }

    //工厂方法
    public static BlogListFragment newInstance() {
        BlogListFragment fragment = new BlogListFragment();
        return fragment;
    }
}
