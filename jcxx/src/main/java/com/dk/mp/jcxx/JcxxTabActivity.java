package com.dk.mp.jcxx;

import android.support.design.widget.TabLayout;
import android.widget.TextView;

import com.dk.mp.core.adapter.MyFragmentPagerAdapter;
import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.widget.MyViewpager;
import com.dk.mp.jcxx.fragment.WjcfFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：janabo on 2017/11/1 09:41
 */
public class JcxxTabActivity extends MyActivity {
    TabLayout mTabLayout;
    MyViewpager mViewpager;
    private TextView mSearch;

    @Override
    protected int getLayoutID() {
        return R.layout.app_jcxx_tab;
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
        titles.add("违纪处分");
        titles.add("荣誉称号");

        for(int i=0;i<titles.size();i++){
            mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(i)));
        }

        List<BaseFragment> fragments = new ArrayList<>();

        fragments.add(new WjcfFragment().newInstance("1","违纪处分"));
        fragments.add(new WjcfFragment().newInstance("2","荣誉称号"));

        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fragments,titles);
        mViewpager.setOffscreenPageLimit ( fragments.size ( ) );
        mViewpager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewpager);
    }
}
