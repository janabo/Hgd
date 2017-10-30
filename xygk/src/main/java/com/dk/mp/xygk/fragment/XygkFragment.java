package com.dk.mp.xygk.fragment;

import android.os.Bundle;
import android.view.View;

import com.android.volley.VolleyError;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.core.widget.OADetailView;
import com.dk.mp.xygk.R;
import com.dk.mp.xygk.entity.Introduction;

import org.json.JSONObject;

/**
 * 作者：janabo on 2017/10/24 09:56
 */
public class XygkFragment extends BaseFragment {
    private Introduction introduction;
    private OADetailView t;
    private String mType;
    private ErrorLayout mError;

    @Override
    protected int getLayoutId() {
        return R.layout.app_intro_desc;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    protected void initialize(View view) {
        super.initialize(view);
        t = (OADetailView) view.findViewById(R.id.content);
        mError = (ErrorLayout) view.findViewById(R.id.error_layout);
        if(DeviceUtil.checkNet()){//判断网络
            getData();
        }else{

        }
    }

    //获取数据
    private void getData(){
        HttpUtil.getInstance().postJsonObjectRequest("apps/introduRest/index", null, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {

            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }


}
