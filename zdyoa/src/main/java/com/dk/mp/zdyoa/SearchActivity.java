package com.dk.mp.zdyoa;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.PageMsg;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.AdapterInterface;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.Logger;
import com.dk.mp.core.util.SnackBarUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.view.MyListView;
import com.dk.mp.core.view.RecycleViewDivider;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.zdyoa.entity.Gzzd;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：janabo on 2017/3/17 11:57
 */
public class SearchActivity extends MyActivity implements View.OnClickListener{
    private EditText mKeywords;//搜索关键字
    private TextView cancle;//
    private LinearLayout layout_search;
    private MyListView mRecycle;
    private List<Gzzd> mList = new ArrayList<>();
    private ErrorLayout mError;
    private String mUrl;
    private String mType;

    @Override
    protected int getLayoutID() {
        return R.layout.app_gzzd_search;
    }

    @Override
    protected void initialize() {
        super.initialize();
        findView();
        if(DeviceUtil.checkNet()){//判断是否有网络
            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
        }else{
            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
        }
    }

    /**
     * 初始化界面
     */
    private void findView(){
        mType = getIntent().getStringExtra("type");
        mUrl = getIntent().getStringExtra("url");
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        mError.setOnLayoutClickListener(this);
        layout_search = (LinearLayout) findViewById(R.id.layout_search);
        mKeywords = (EditText) findViewById(R.id.search_Keywords);
        cancle = (TextView) findViewById(R.id.cancle_search);
        mRecycle = (MyListView) findViewById(R.id.person_recycle);
        mRecycle.setLayoutManager ( new LinearLayoutManager( mContext ) );
        mRecycle.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, DeviceUtil.dip2px(mContext,0.8f), Color.rgb(229, 229, 229)));
        mRecycle.setAdapterInterface(mList, new AdapterInterface() {
            @Override
            public RecyclerView.ViewHolder setItemView(ViewGroup parent, int viewType) {
                View view =  LayoutInflater.from(mContext).inflate(R.layout.app_gzzd_list_item, parent, false);// 设置要转化的layout文件
                return new MyView(view);
            }

            @Override
            public void setItemValue(RecyclerView.ViewHolder holder, int position) {
                Gzzd m = mList.get(position);
                ((MyView)holder).ssl_fjh.setText(m.getTitle());
                if("gzzd".equals(mType)){//规章制度
                    ((MyView)holder).ssq.setText(m.getDate());
                    ((MyView)holder).fs.setText(m.getUser());
                }else if("tzgg".equals(mType)) {//通知公告
                    ((MyView)holder).ssq.setText(m.getTime());
                    ((MyView)holder).fs.setText(m.getUser());
                }else{//值班安排
                    ((MyView)holder).ssq.setText(m.getSubTitle());
                    ((MyView)holder).fs.setText("");
                }
            }

            @Override
            public void loadDatas() {
                getData();
            }
        });

        mKeywords.setHint("关键字搜索");
        mKeywords.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == 0 || actionId == 3) && event != null) {
                    final String keywords = mKeywords.getText().toString();
                    Logger.info(keywords);
                    hideSoftInput();
                    if (StringUtils.isNotEmpty(keywords)) {
                        mError.setErrorType(ErrorLayout.LOADDATA);
                        getData();
                        mList.clear();
                    } else {
                        SnackBarUtil.showShort(layout_search,"请输入关键字");
                    }
                }else if(actionId == 3 && event == null){
                    final String keywords = mKeywords.getText().toString();
                    if (!StringUtils.isNotEmpty(keywords)) {
                        hideSoftInput();
                        SnackBarUtil.showShort(layout_search,"请输入关键字");
                    }
                }
                return false;
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    public void getData(){
        if(DeviceUtil.checkNet()) {
            mList.clear();
            mRecycle.clearList();
            query();
        }else{
            if(mRecycle.pageNo == 1) {
                mError.setErrorType(ErrorLayout.NETWORK_ERROR);
            }else{
                SnackBarUtil.showShort(mRecycle,R.string.net_no2);
            }
        }
    }

    public void query(){
        Map<String, Object> map = new HashMap<>();
        map.put("key", mKeywords.getText().toString());
        map.put("pageNo",mRecycle.pageNo);
        HttpUtil.getInstance().gsonRequest(new TypeToken<PageMsg<Gzzd>>(){}, mUrl, map, new HttpListener<PageMsg<Gzzd>>() {
            @Override
            public void onSuccess(PageMsg<Gzzd> result) {
                mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                if(result.getList() != null && result.getList().size()>0) {
                    mList.addAll(result.getList());
                    mRecycle.finish(result.getTotalPages(),result.getCurrentPage());
                }else{
                    if(mRecycle.pageNo == 1) {
                        mError.setErrorType(ErrorLayout.SEARCHNODATA);
                    }else{
                        SnackBarUtil.showShort(mRecycle,R.string.searchnodata);
                    }
                }
            }
            @Override
            public void onError(VolleyError error) {
                if(mRecycle.pageNo == 1) {
                    mError.setErrorType(ErrorLayout.DATAFAIL);
                }else{
                    SnackBarUtil.showShort(mRecycle,R.string.data_fail);
                }
            }
        });
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInput(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    @Override
    public void onClick(View view) {
        getData();
    }


    class MyView extends RecyclerView.ViewHolder{
        public TextView ssl_fjh;
        public TextView ssq;
        public TextView fs;

        public MyView(View itemView) {
            super(itemView);
            ssl_fjh = (TextView) itemView.findViewById(R.id.ssl_fjh);
            ssq = (TextView) itemView.findViewById(R.id.ssq);
            fs = (TextView) itemView.findViewById(R.id.fs);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Gzzd sswz = mList.get(getLayoutPosition());
                    Intent intent = new Intent(mContext, HttpWebActivity.class);
                    intent.putExtra("title",getIntent().getStringExtra("title"));
                    intent.putExtra("url",sswz.getUrl());
                    startActivity(intent);
                }
            });

        }
    }

}
