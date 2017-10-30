package com.dk.mp.xygk.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.SnackBarUtil;
import com.dk.mp.core.view.MyListView;
import com.dk.mp.core.view.RecycleViewDivider;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.xygk.R;
import com.dk.mp.xygk.entity.Depart;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：janabo on 2017/10/24 10:36
 */
public class XygkCollegeFragment extends BaseFragment implements View.OnClickListener{
    List<Depart> mDatas = new ArrayList<>();
    private ErrorLayout mError;
    private MyListView myListView;

    @Override
    protected int getLayoutId() {
        return R.layout.app_intro_college;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    protected void initialize(View view) {
        super.initialize(view);
        mError = (ErrorLayout) view.findViewById(R.id.error_layout);
        myListView = (MyListView)view.findViewById(R.id.mList);
        mError.setOnLayoutClickListener(this);

    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        initViews();
    }

    public void initViews(){
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        myListView.setLayoutManager(manager);
        myListView.addItemDecoration(new RecycleViewDivider(mContext, GridLayoutManager.HORIZONTAL, 1, Color.rgb(201, 201, 201)));//添加分割线
    }

    public void getData(){
        if(DeviceUtil.checkNet()) {
        }else{
            if(myListView.pageNo == 1) {
                mError.setErrorType(ErrorLayout.NETWORK_ERROR);
            }else{
                SnackBarUtil.showShort(myListView,R.string.net_no2);
            }
        }
    }



    @Override
    public void onClick(View v) {

    }
}
