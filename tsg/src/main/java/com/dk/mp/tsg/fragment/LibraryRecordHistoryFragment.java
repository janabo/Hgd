package com.dk.mp.tsg.fragment;

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
import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.util.AdapterInterface;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.SnackBarUtil;
import com.dk.mp.core.view.MyListView;
import com.dk.mp.core.view.RecycleViewDivider;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.tsg.entity.BookRecord;
import com.dk.mp.tsg.R;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：janabo on 2017/10/25 11:21
 */
public class LibraryRecordHistoryFragment extends BaseFragment implements View.OnClickListener{
    ErrorLayout mError;
    private MyListView myListView;
    private List<BookRecord> list = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.app_tsg_history;
    }

    @Override
    protected void initialize(View view) {
        super.initialize(view);
        myListView = (MyListView) view.findViewById(R.id.newslist);
        mError = (ErrorLayout) view.findViewById(R.id.error_layout);
        mError.setOnLayoutClickListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        myListView.setLayoutManager(manager);
        myListView.addItemDecoration(new RecycleViewDivider(getContext(), GridLayoutManager.HORIZONTAL, 1, Color.rgb(201, 201, 201)));//添加分割线
        myListView.setAdapterInterface(list, new AdapterInterface() {
            @Override
            public RecyclerView.ViewHolder setItemView(ViewGroup parent, int viewType) {
                View view =  LayoutInflater.from(mContext).inflate(R.layout.app_tsg_item, parent, false);// 设置要转化的layout文件
                return new MyView(view);
            }

            @Override
            public void setItemValue(RecyclerView.ViewHolder holder, int position) {
                BookRecord j = list.get(position);
                ((MyView) holder).name.setText(j.getName());
                ((MyView) holder).jssj.setText(j.getJssj());
                ((MyView) holder).ghsj.setText(j.getYjhssj());
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
            }else{
                SnackBarUtil.showShort(myListView,R.string.net_no2);
            }
        }
    }

    public void getList(){
        myListView.startRefresh();
        Map<String,Object> map = new HashMap<>();
        map.put("pageNo",myListView.pageNo);
        HttpUtil.getInstance().gsonRequest(new TypeToken<PageMsg<BookRecord>>(){}, "apps/tsg/getHistory", map, new HttpListener<PageMsg<BookRecord>>() {
            @Override
            public void onSuccess(PageMsg<BookRecord> result) {
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
        private TextView name, jssj,ghsj;
        public MyView(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            jssj = (TextView) itemView.findViewById(R.id.jssj);
            ghsj = (TextView) itemView.findViewById(R.id.ghsj);
        }
    }
}
