package com.dk.mp.jcxx;

import android.content.Intent;

import com.android.volley.VolleyError;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.core.widget.OADetailView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * 作者：janabo on 2017/11/1 11:24
 */
public class JcxxDetailActivity extends MyActivity{
    private OADetailView content;
    private String id;
    private String type;
    private ErrorLayout mError;

    @Override
    protected int getLayoutID() {
        return R.layout.jcxx_detail;
    }

    @Override
    protected void initView() {
        super.initView();
        Intent intent = getIntent();
        setTitle(intent.getStringExtra("title"));
        id = intent.getStringExtra("id");
        type = intent.getStringExtra("mType");
        mError = (ErrorLayout)findViewById(R.id.errorlayout);
        content = (OADetailView) findViewById(R.id.content);
        if(DeviceUtil.checkNet()){
            getData();
        }else{
            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
        }

    }

    public void getData(){
        Map<String,Object> map = new HashMap<>();
        map.put("type",type);
        map.put("id", id);
        HttpUtil.getInstance().postJsonObjectRequest("apps/jcxx/detail", map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result)  {
                try {
                    if (result.getInt("code") != 200) {
                        mError.setErrorType(ErrorLayout.DATAFAIL);
                    }else{
                        mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                        String detail = result.optString("data");
                        if(StringUtils.isNotEmpty(detail)){
                            content.setText(detail);
                        }else{
                            mError.setErrorType(ErrorLayout.NODATA);
                        }
                    }
                }catch (Exception e){
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
