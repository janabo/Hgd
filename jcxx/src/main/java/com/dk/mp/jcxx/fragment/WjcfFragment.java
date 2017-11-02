package com.dk.mp.jcxx.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.PageMsg;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.util.AdapterInterface;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.SnackBarUtil;
import com.dk.mp.core.view.MyListView;
import com.dk.mp.core.view.RecycleViewDivider;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.jcxx.JcxxDetailActivity;
import com.dk.mp.jcxx.R;
import com.dk.mp.jcxx.entity.Jcxx;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 违纪处分
 * 作者：janabo on 2017/11/1 10:43
 */
public class WjcfFragment extends BaseFragment implements View.OnClickListener{
    public static final String ARGS_TABS = "args_tabs";
    public static final String ARGS_TABS_TITLE = "args_tabs_title";
    private ErrorLayout mError;
    private MyListView myListView;
    private String mType,mTitle;
    private List<Jcxx> mData = new ArrayList<>();

    public static WjcfFragment newInstance(String type,String title) {
        Bundle args = new Bundle();
        args.putString(ARGS_TABS,type);
        args.putString(ARGS_TABS_TITLE,title);
        WjcfFragment fragment = new WjcfFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getArguments().getString(ARGS_TABS);
        mTitle = getArguments().getString(ARGS_TABS_TITLE);
    }


    @Override
    protected void initialize(View view) {
        super.initialize(view);
        mError = (ErrorLayout) view.findViewById(R.id.error_layout);
        myListView = (MyListView)view.findViewById(R.id.newslist);
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
        myListView.setAdapterInterface(mData, new AdapterInterface() {
            @Override
            public RecyclerView.ViewHolder setItemView(ViewGroup parent, int viewType) {
                View view =  LayoutInflater.from(mContext).inflate(R.layout.jcxx_item, parent, false);// 设置要转化的layout文件
                return new WjcfFragment.MyView(view);
            }
            @Override
            public void setItemValue(RecyclerView.ViewHolder holder, int position) {
                Jcxx j = mData.get(position);
                ((MyView) holder).name.setText(j.getName());
                ((MyView) holder).subtitle.setText(j.getSubTitle());
            }
            @Override
            public void loadDatas() {
                getData();
            }
        });
        getData();
    }

    public void getData(){
        if(DeviceUtil.checkNet()) {
            getList();
        }else{
            if(myListView.pageNo == 1) {
                mError.setErrorType(ErrorLayout.NETWORK_ERROR);
            }else{
                SnackBarUtil.showShort(myListView,R.string.net_no2);
            }
        }
    }

    public void getList(){
        myListView.startRefresh();
        Map<String,Object> map = new HashMap<>();
        map.put("pageNo",myListView.pageNo);
        map.put("type",mType);
        HttpUtil.getInstance().gsonRequest(new TypeToken<PageMsg<Jcxx>>(){}, "apps/jcxx/list", map, new HttpListener<PageMsg<Jcxx>>() {
            @Override
            public void onSuccess(PageMsg<Jcxx> result) {
                myListView.stopRefresh(true);
                mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                if(result.getList() != null && result.getList().size()>0) {//是否获取到数据
                    mData.addAll(result.getList());
                    if(myListView.getAdapter()!=null) {
                        myListView.getAdapter().notifyDataSetChanged();
                        myListView.flish();
                    }
                }else{
                    if(myListView.pageNo == 1) {//是否是第一页
                        mError.setErrorType(ErrorLayout.NODATA);
                    }else{
                        SnackBarUtil.showShort(myListView,R.string.nodata);
                    }
                }
            }
            @Override
            public void onError(VolleyError error) {
                if(myListView.pageNo == 1) {
                    mError.setErrorType(ErrorLayout.DATAFAIL);
                }else{
                    SnackBarUtil.showShort(myListView,R.string.data_fail);
                }
            }
        });
    }

    private class MyView extends RecyclerView.ViewHolder{
        private TextView name,subtitle;
        public MyView(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            subtitle = (TextView) itemView.findViewById(R.id.subtitle);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Jcxx jcxx = mData.get(getLayoutPosition());
                    Intent intent = new Intent(mContext,JcxxDetailActivity.class);
                    intent.putExtra("title", mTitle);
                    intent.putExtra("id", jcxx.getId());
                    intent.putExtra("mType", mType);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        getData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.jcxx_list;
    }
}
