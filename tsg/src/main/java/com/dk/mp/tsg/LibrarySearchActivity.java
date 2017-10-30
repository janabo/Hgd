package com.dk.mp.tsg;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import com.dk.mp.tsg.entity.Book;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dk.mp.core.application.MyApplication.getContext;

/**
 * 作者：janabo on 2017/10/25 13:36
 */
public class LibrarySearchActivity extends MyActivity{
    private EditText searchKeywords;
    private List<Book> mData = new ArrayList<>();
    private TextView cancle_search;
    private ErrorLayout mError;
    private MyListView mListView;

    @Override
    protected int getLayoutID() {
        return R.layout.app_tsg_query;
    }

    @Override
    protected void initView() {
        super.initView();
        mListView = (MyListView) findViewById(R.id.newslist);
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
        cancle_search = (TextView) findViewById(R.id.cancle_search);
        searchKeywords = (EditText) findViewById(R.id.search_Keywords);
        searchKeywords.setHint("搜索图书名称或者关键字");
        searchKeywords.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == 0 || actionId == 3) && event != null) {
                    final String keywords = searchKeywords.getText().toString();
                    Logger.info(keywords);
                    hideSoftInput();
                    if (StringUtils.isNotEmpty(keywords)) {
                        mData.clear();
                        getData();
                    } else {
                        SnackBarUtil.showShort(mListView,"请输入图书名称或者关键字");
                    }
                }else if(actionId == 3 && event == null){
                    String keywords = searchKeywords.getText().toString();
                    if (!StringUtils.isNotEmpty(keywords)) {
                        SnackBarUtil.showShort(mListView,"请输入图书名称或者关键字");
                    }
                }
                return false;
            }
        });
        cancle_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initialize() {
        super.initialize();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mListView.setLayoutManager(manager);
        mListView.addItemDecoration(new RecycleViewDivider(getContext(), GridLayoutManager.HORIZONTAL, 1, Color.rgb(201, 201, 201)));//添加分割线
        mListView.setAdapterInterface(mData, new AdapterInterface() {
            @Override
            public RecyclerView.ViewHolder setItemView(ViewGroup parent, int viewType) {
                View view =  LayoutInflater.from(mContext).inflate(R.layout.app_tsg_query_item, parent, false);// 设置要转化的layout文件
                return new MyView(view);
            }

            @Override
            public void setItemValue(RecyclerView.ViewHolder holder, int position) {
                Book j = mData.get(position);
                ((MyView) holder).name.setText(j.getName());
                ((MyView) holder).jssj.setText(j.getStock()+"本");
            }
            @Override
            public void loadDatas() {
                getData();
            }
        });
    }

    public void getData(){
        if (DeviceUtil.checkNet()) {
            query();
        }else{
            if(mListView.pageNo == 1){
                mListView.setVisibility(View.GONE);
                mError.setErrorType(ErrorLayout.NETWORK_ERROR);
            }else{
                mListView.setVisibility(View.GONE);
                SnackBarUtil.showShort(mListView,R.string.net_no2);
            }
        }
    }

    public void query(){
        mListView.startRefresh();
        Map<String,Object> map = new HashMap<>();
        map.put("key", searchKeywords.getText().toString());
        map.put("pageNo",mListView.pageNo);
        HttpUtil.getInstance().gsonRequest(new TypeToken<PageMsg<Book>>(){}, "apps/tsg/query", map, new HttpListener<PageMsg<Book>>() {
            @Override
            public void onSuccess(PageMsg<Book> result) {
                mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                if(result.getList() != null && result.getList().size()>0) {
                    mData.addAll(result.getList());
                    mListView.finish(result.getTotalPages(),result.getCurrentPage());
                }else{
                    if(mListView.pageNo == 1) {
                        mError.setErrorType(ErrorLayout.SEARCHNODATA);
                    }else{
                        SnackBarUtil.showShort(mListView,R.string.searchnodata);
                    }
                }
            }
            @Override
            public void onError(VolleyError error) {
                if(mListView.pageNo == 1) {
                    mError.setErrorType(ErrorLayout.DATAFAIL);
                }else{
                    SnackBarUtil.showShort(mListView,R.string.data_fail);
                }
            }
        });
    }

    private class MyView extends RecyclerView.ViewHolder{
        private TextView name, jssj;
        public MyView(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            jssj = (TextView) itemView.findViewById(R.id.jssj);
        }
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInput(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }
}
