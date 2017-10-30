package com.dk.mp.tsg.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.VolleyError;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.view.RecycleViewDivider;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.tsg.entity.BookRecord;
import com.dk.mp.tsg.R;
import com.dk.mp.tsg.adapter.RecordAdapter;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 在借图书
 * 作者：janabo on 2017/10/25 10:52
 */
public class LibraryRecordFragment extends BaseFragment {
    private ErrorLayout mError;
    private RecyclerView mListView;
    private List<BookRecord> mData = new ArrayList<>();
    private RecordAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.app_tsg_list;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    protected void initialize(View view) {
        super.initialize(view);
        mListView = (RecyclerView) view.findViewById(R.id.listview);
        mError = (ErrorLayout) view.findViewById(R.id.error_layout);
        mListView.setHasFixedSize ( true );
        mListView.setLayoutManager ( new LinearLayoutManager( mContext ) );
        mAdapter = new RecordAdapter(mContext,mData);
        mListView.setAdapter(mAdapter);
        mListView.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, DeviceUtil.dip2px(mContext,0.8f), Color.rgb(229, 229, 229)));
        if(DeviceUtil.checkNet()){//判断网络
            getData();
        }else{
            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
        }
    }

    //获取数据
    private void getData(){
        HttpUtil.getInstance().postJsonObjectRequest("apps/tsg/getBorrowNow", null, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    JSONArray array = result.getJSONArray("data");
                    if(array == null || array.length()<=0){
                        mError.setErrorType(ErrorLayout.NODATA);
                    }else {
                        mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                        for (int i=0;i<array.length();i++){
                            BookRecord b = new Gson().fromJson(array.get(i).toString(),BookRecord.class);
                            mData.add(b);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mError.setErrorType(ErrorLayout.DATAFAIL);
                }
            }

            @Override
            public void onError(VolleyError error) {
                mError.setErrorType(ErrorLayout.DATAFAIL);
            }
        });
    }
}
