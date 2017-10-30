package com.dk.mp.ykt;

import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.view.RecycleViewDivider;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.ykt.adapter.MonthAdapter;
import com.dk.mp.ykt.entity.CardInfo;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.dk.mp.core.util.TimeUtils.getBeforeMonth;

/**
 * 一卡通
 * 作者：janabo on 2017/10/26 11:32
 */
public class CardMainActivity extends MyActivity{
    private TextView money, name, cardNo, title_txt;
    private CoreSharedPreferencesHelper h;
    private ErrorLayout mError;
    private RecyclerView mListView;
    private MonthAdapter mAdapter;
    private List<String> mData = new ArrayList<>();

    @Override
    protected int getLayoutID() {
        return R.layout.app_card_main;
    }

    @Override
    protected void initView() {
        super.initView();
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, com.dk.mp.core.R.color.card_org));
        }

        h = new CoreSharedPreferencesHelper(mContext);
        setTitle(getIntent().getStringExtra("title"));
        money = (TextView)findViewById(R.id.money);
        name = (TextView)findViewById(R.id.name);
        cardNo = (TextView)findViewById(R.id.cardNo);
        title_txt = (TextView)findViewById(R.id.title_txt);
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        mListView = (RecyclerView) findViewById(R.id.listview);
        mListView.setHasFixedSize ( true );
        mListView.setLayoutManager ( new LinearLayoutManager( mContext ) );
        mAdapter= new MonthAdapter(mContext,getIntent().getStringExtra("title"),"",mData);
        mListView.setAdapter(mAdapter);
        mListView.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, DeviceUtil.dip2px(mContext,0.8f), Color.rgb(229, 229, 229)));
    }

    @Override
    protected void initialize() {
        super.initialize();
        if(DeviceUtil.checkNet()){//判断网络
            getInfo();
        }else{
            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
            mListView.setVisibility(View.GONE);
        }
    }

    /**
     * 获取数据
     */
    public void getInfo(){
        HttpUtil.getInstance().postJsonObjectRequest("apps/ykt/getInfo", null, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result != null && result.getInt("code") == 200){
                        if(result.optJSONObject("data") != null){
                            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                            mListView.setVisibility(View.VISIBLE);
                            CardInfo card = new Gson().fromJson(result.optJSONObject("data").toString(),CardInfo.class);
                            DecimalFormat fnum = new DecimalFormat("##0.0");
                            String dd=fnum.format(Float.valueOf(card.getMoney()));
                            money.setText(dd + "元");
                            title_txt.setText("余额");
                            name.setText("持卡人：" + card.getUserName());
                            cardNo.setText("卡号：" + card.getCardNo());
                            for(int i=0;i<6;i++){
                                mData.add(getBeforeMonth(i));
                            }
                            mAdapter.setCardno(card.getCardNo());
                            mAdapter.notifyDataSetChanged();
                        }else{
                            mError.setErrorType(ErrorLayout.NODATA);
                            money.setText("");
                            title_txt.setText("_ _ 余额");
                            name.setText("持卡人：" + "_ _");
                            cardNo.setText("卡号：" + "_ _");
                            mListView.setVisibility(View.GONE);
                        }
                    }else{
                        mError.setErrorType(ErrorLayout.DATAFAIL);
                        mListView.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mError.setErrorType(ErrorLayout.DATAFAIL);
                    mListView.setVisibility(View.GONE);
                }
            }
            @Override
            public void onError(VolleyError error) {
                mError.setErrorType(ErrorLayout.DATAFAIL);
                mListView.setVisibility(View.GONE);
            }
        });
    }
}
