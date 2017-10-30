package com.dk.mp.gzcx;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.TimeUtils;
import com.dk.mp.core.view.RecycleViewDivider;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.gzcx.adapter.SalaryAdapter;
import com.dk.mp.gzcx.entity.Salary;

import java.util.ArrayList;
import java.util.List;

/**
 * 工资查询
 * 作者：janabo on 2017/10/24 11:46
 */
public class SalaryActivity extends MyActivity {
    private ErrorLayout mError;
    private RecyclerView mList;
    private List<Salary> mData = new ArrayList<>();
    SalaryAdapter mAdapter;


    @Override
    protected int getLayoutID() {
        return R.layout.app_salary;
    }

    @Override
    protected void initView() {
        super.initView();
        setTitle(getIntent().getStringExtra("title"));
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        mList = (RecyclerView) findViewById(R.id.mListView);
        mList.setHasFixedSize ( true );
        mList.setLayoutManager ( new LinearLayoutManager( mContext ) );
        mAdapter = new SalaryAdapter(mContext,mData);
        mList.setAdapter(mAdapter);
        mList.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, DeviceUtil.dip2px(mContext,0.8f), Color.rgb(229, 229, 229)));
    }

    @Override
    protected void initialize() {
        super.initialize();
        for(int i=0;i<12;i++){//显示12个月的工资查询
            Salary s = new Salary();
            String rq = TimeUtils.getBeforeMonth(i);
            s.setTitle(rq.replace("-", "年")+"月工资账单");
            s.setRq(rq);
            mData.add(s);
        }
        mAdapter.notifyDataSetChanged();
        mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
    }
}
