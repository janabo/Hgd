package com.dk.mp.tzgg;

import android.content.Intent;
import android.graphics.Color;
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
import com.dk.mp.core.ui.HttpWebActivity;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.AdapterInterface;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.SnackBarUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.view.MyListView;
import com.dk.mp.core.view.RecycleViewDivider;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.tzgg.entity.Notice;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 作者：janabo on 2017/10/26 10:07
 */
public class NoticeListActivity extends MyActivity implements View.OnClickListener{
    ErrorLayout mError;
    private MyListView myListView;
    private List<Notice> list = new ArrayList<>();

    @Override
    protected int getLayoutID() {
        return R.layout.app_notice;
    }

    @Override
    protected void initialize() {
        super.initialize();
        setTitle(getIntent().getStringExtra("title"));

        myListView = (MyListView) findViewById(R.id.newslist);
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        mError.setOnLayoutClickListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        myListView.setLayoutManager(manager);
        myListView.addItemDecoration(new RecycleViewDivider(this, GridLayoutManager.HORIZONTAL, 1, Color.rgb(201, 201, 201)));//添加分割线

        myListView.setAdapterInterface(list, new AdapterInterface() {
            @Override
            public RecyclerView.ViewHolder setItemView(ViewGroup parent, int viewType) {
                View view =  LayoutInflater.from(mContext).inflate(R.layout.app_notice_item, parent, false);// 设置要转化的layout文件
                return new MyView(view);
            }

            @Override
            public void setItemValue(RecyclerView.ViewHolder holder, int position) {

                ((MyView)holder).title.setText(list.get(position).getTitle());
                ((MyView)holder).author.setText(StringUtils.checkEmpty(list.get(position).getAuthor()));
                ((MyView)holder).time.setText(StringUtils.checkEmpty(list.get(position).getPublishTime()));
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
            if(myListView.pageNo == 1){
                mError.setErrorType(ErrorLayout.NETWORK_ERROR);
            }else {
                SnackBarUtil.showShort(myListView,R.string.net_no2);
            }
        }
    }

    public void getList(){
        myListView.startRefresh();
        Map<String,Object> map = new HashMap<>();
        map.put("pageNo",myListView.pageNo);
        HttpUtil.getInstance().gsonRequest(new TypeToken<PageMsg<Notice>>(){}, "apps/notice/getList", map, new HttpListener<PageMsg<Notice>>() {
            @Override
            public void onSuccess(PageMsg<Notice> result) {
                mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                if(result.getList() != null && result.getList().size()>0) {
                    list.addAll(result.getList());
                    myListView.finish(result.getTotalPages(),result.getCurrentPage());
                }else{
                    if(myListView.pageNo == 1) {
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

    @Override
    public void onClick(View view) {
        getData();
    }

    private class MyView extends RecyclerView.ViewHolder{
        private TextView title,author,time;
        public MyView(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);// 取得实例
            author = (TextView) itemView.findViewById(R.id.author);// 取得实例
            time = (TextView) itemView.findViewById(R.id.time);// 取得实例

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Notice news = list.get(getLayoutPosition());
                    Intent intent;
                    if(news.getUrl() == null || news.getUrl().equals("") || news.getUrl().equals("null")){
                        intent = new Intent(mContext, NoticeHttpWebActivity.class);
                        intent.putExtra("title", "通知公告");
                        intent.putExtra("contenttitle", news.getTitle());
                        intent.putExtra("time", news.getPublishTime());
                        intent.putExtra("url", news.getContent());
                    }else{
                        intent = new Intent(mContext, HttpWebActivity.class);
                        intent.putExtra("title", "通知公告");
                        intent.putExtra("url", news.getUrl());
                    }
                    startActivity(intent);
                }
            });
        }
    }

}
