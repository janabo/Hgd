package com.dk.mp.zdyoa;

import android.graphics.Color;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.view.RecycleViewDivider;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.zdyoa.adapter.ManagerAdapter;
import com.dk.mp.zdyoa.entity.OaItemEntity;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：janabo on 2017/11/2 10:18
 */
public class ManagerActivity extends MyActivity{
    LinearLayout oprition_layout,other_layout;//最近使用，业务管理，其它
    RecyclerView oprition_recycler_view,other_recycler_view;
    ManagerAdapter bAdapter;//业务管理
    ManagerAdapter oAdapter;//其它
    List<OaItemEntity> bData = new ArrayList<>();
    List<OaItemEntity> oData = new ArrayList<>();
    private ErrorLayout mError;


    @Override
    protected int getLayoutID() {
        return R.layout.main_manager;
    }

    @Override
    protected void initView() {
        super.initView();
        setTitle(getIntent().getStringExtra("title"));
        mError = (ErrorLayout) findViewById(R.id.main_error);
        oprition_layout = (LinearLayout) findViewById(R.id.oprition_layout);
        other_layout = (LinearLayout) findViewById(R.id.other_layout);
        oprition_recycler_view = (RecyclerView) findViewById(R.id.oprition_recycler_view);
        other_recycler_view = (RecyclerView) findViewById(R.id.other_recycler_view);
        RecycleViewDivider vDivider = new RecycleViewDivider(mContext, GridLayoutManager.VERTICAL, 1, Color.rgb(201, 201, 201));
        RecycleViewDivider hDivider = new RecycleViewDivider(mContext, GridLayoutManager.HORIZONTAL, 1, Color.rgb(201, 201, 201));
        RecyclerView.ItemAnimator animator = new DefaultItemAnimator();

        bAdapter = new ManagerAdapter(mContext,bData,getSharedPreferences());
        oprition_recycler_view.setLayoutManager(new GridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false));
        oprition_recycler_view.setAdapter(bAdapter);
        oprition_recycler_view.setItemAnimator(animator);
        oprition_recycler_view.addItemDecoration(vDivider);//添加分割线
        oprition_recycler_view.addItemDecoration(hDivider);//添加分割线

        oAdapter = new ManagerAdapter(mContext,oData,getSharedPreferences());
        other_recycler_view.setLayoutManager(new GridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false));
        other_recycler_view.setAdapter(oAdapter);
        other_recycler_view.setItemAnimator(animator);
        other_recycler_view.addItemDecoration(vDivider);//添加分割线
        other_recycler_view.addItemDecoration(hDivider);//添加分割线
        if(DeviceUtil.checkNet()) {
            getData();
        }else{
            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
        }
    }

    public void getData(){
        HttpUtil.getInstance().postJsonObjectRequest("apps/oa/listModule", null, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if (result.optInt("code") == 200){//成功返回数据
                    try {
                        List<OaItemEntity> oaItemEntityList = getGson().fromJson(result.getJSONArray("data").toString(),new TypeToken<List<OaItemEntity>>(){}.getType());
                        if (oaItemEntityList == null){
                            mError.setErrorType(ErrorLayout.DATAFAIL);
                        } else if (oaItemEntityList.size() == 0) {
                            mError.setErrorType(ErrorLayout.NODATA);
                        } else {
                            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                            bData.clear();
                            oData.clear();
                            for(OaItemEntity o : oaItemEntityList){
                                if(StringUtils.isNotEmpty(o.getDetailUrl()))
                                    bData.add(o);
                                else
                                    oData.add(o);
                            }
                            if(bData.size()>0)
                                oprition_layout.setVisibility(View.VISIBLE);
                            else
                                oprition_layout.setVisibility(View.GONE);
                            if(oData.size()>0)
                                other_layout.setVisibility(View.VISIBLE);
                            else
                                other_layout.setVisibility(View.GONE);

                            bAdapter.notifyDataSetChanged();
                            oAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        mError.setErrorType(ErrorLayout.DATAFAIL);
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {
                mError.setErrorType(ErrorLayout.DATAFAIL);
            }
        });
    }

}
