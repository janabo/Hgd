package com.dk.mp.xygk;

import android.support.design.widget.TabLayout;

import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.widget.MyViewpager;
import com.dk.mp.xygk.adapter.MyFragmentPagerAdapter;
import com.dk.mp.xygk.fragment.XygkCollegeFragment;
import com.dk.mp.xygk.fragment.XygkFragment;
import com.dk.mp.xygk.fragment.XygkHistoryFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 学院概况
 * 作者：janabo on 2017/10/24 09:33
 */
public class XygkTabActivity extends MyActivity {
    TabLayout mTabLayout;
    MyViewpager mViewpager;

    @Override
    protected int getLayoutID() {
        return R.layout.app_xygk_tab;
    }

    @Override
    protected void initView() {
        super.initView();
        setTitle(getIntent().getStringExtra("title"));
        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mViewpager = (MyViewpager) findViewById(R.id.viewpager);
    }

    @Override
    protected void initialize() {
        super.initialize();
        List<String> titles = new ArrayList<>();
        titles.add("学校简介");
        titles.add("校史沿革");
        titles.add("院系介绍");

        for(int i=0;i<titles.size();i++){
            mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(i)));
        }

        List<BaseFragment> fragments = new ArrayList<>();

        fragments.add(new XygkFragment());
        fragments.add(new XygkHistoryFragment());
        fragments.add(new XygkCollegeFragment());

        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fragments,titles);
        mViewpager.setOffscreenPageLimit ( fragments.size ( ) );
        mViewpager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewpager);
    }
}
