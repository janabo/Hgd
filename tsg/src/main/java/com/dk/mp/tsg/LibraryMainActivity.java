package com.dk.mp.tsg;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.TextView;

import com.dk.mp.core.adapter.MyFragmentPagerAdapter;
import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.widget.MyViewpager;
import com.dk.mp.tsg.fragment.LibraryRecordFragment;
import com.dk.mp.tsg.fragment.LibraryRecordHistoryFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：janabo on 2017/10/25 10:37
 */
public class LibraryMainActivity extends MyActivity {
    TabLayout mTabLayout;
    MyViewpager mViewpager;
    private TextView mSearch;

    @Override
    protected int getLayoutID() {
        return R.layout.app_tsg_tab;
    }
    @Override
    protected void initView() {
        super.initView();
        setTitle(getIntent().getStringExtra("title"));
        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mViewpager = (MyViewpager) findViewById(R.id.viewpager);
        mSearch = (TextView) findViewById(R.id.right_txt);
    }

    @Override
    protected void initialize() {
        super.initialize();
        List<String> titles = new ArrayList<>();
        titles.add("在借图书");
        titles.add("借阅记录");

        for(int i=0;i<titles.size();i++){
            mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(i)));
        }

        List<BaseFragment> fragments = new ArrayList<>();

        fragments.add(new LibraryRecordFragment());
        fragments.add(new LibraryRecordHistoryFragment());

        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fragments,titles);
        mViewpager.setOffscreenPageLimit ( fragments.size ( ) );
        mViewpager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewpager);

        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LibraryMainActivity.this, LibrarySearchActivity.class);
                startActivity(intent);
            }
        });
    }
}
