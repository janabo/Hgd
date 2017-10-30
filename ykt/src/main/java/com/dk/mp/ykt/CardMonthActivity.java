package com.dk.mp.ykt;

import android.view.View;

import com.android.volley.VolleyError;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.core.widget.OADetailView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 一卡通
 * 作者：janabo on 2017/10/26 11:28
 */
public class CardMonthActivity extends MyActivity {
    private OADetailView content;
    private String month;
    private ErrorLayout mError;

    @Override
    protected int getLayoutID() {
        return R.layout.app_card_detail;
    }

    @Override
    protected void initView() {
        super.initView();
        setTitle(getIntent().getStringExtra("title"));
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        content = (OADetailView) findViewById(R.id.content);
        month = getIntent().getStringExtra("month");
    }

    @Override
    protected void initialize() {
        super.initialize();
        if(DeviceUtil.checkNet()){
            getDetail();
        }else{
            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
        }
    }

    public void getDetail() {
        Map<String, Object> map = new HashMap<>();
        map.put("month", month.replace("年", "-").replace("月", ""));
        map.put("cardNo", getIntent().getStringExtra("cardNo"));
        HttpUtil.getInstance().postJsonObjectRequest("apps/ykt/list", map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result != null && result.getInt("code") == 200){
                        if(result.optString("data") != null){
                            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                            content.setVisibility(View.VISIBLE);
                            String str = result.getString("data");
                            content.setText(StringUtils.checkEmpty(str));
                        }else{
                            mError.setErrorType(ErrorLayout.NODATA);
                            content.setVisibility(View.GONE);
                        }
                    }else{
                        mError.setErrorType(ErrorLayout.DATAFAIL);
                        content.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mError.setErrorType(ErrorLayout.DATAFAIL);
                    content.setVisibility(View.GONE);
                }
            }
            @Override
            public void onError(VolleyError error) {
                mError.setErrorType(ErrorLayout.DATAFAIL);
                content.setVisibility(View.GONE);
            }
        });

    }
}
