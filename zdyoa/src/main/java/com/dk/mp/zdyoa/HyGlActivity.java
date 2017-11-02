package com.dk.mp.zdyoa;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.dk.mp.core.entity.LoginMsg;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.Logger;
import com.dk.mp.core.util.TimeUtils;
import com.dk.mp.core.widget.ErrorLayout;

import java.util.Calendar;
import java.util.Date;

/**
 * 会议管理
 * 作者：janabo on 2017/3/20 11:30
 */
public class HyGlActivity extends MyActivity {
    private ErrorLayout mError;
    private WebView mWebView;
    private TimePickerView pvCustomTime;
    private LinearLayout dropdown;
    @Override
    protected int getLayoutID() {
        return R.layout.app_hygl;
    }

    @Override
    protected void initView() {
        super.initView();
        dropdown = (LinearLayout) findViewById(R.id.dropdown);
        setTitle(TimeUtils.getToday()+" "+ TimeUtils.getWeekDayInt2Str(TimeUtils.getWeek(TimeUtils.getToday())));
        mWebView = (WebView) findViewById(R.id.webview);
        mError = (ErrorLayout)findViewById(R.id.error_layout);
        initCustomTimePicker();
        dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvCustomTime.show();
            }
        });
        LoginMsg loginMsg = getSharedPreferences().getLoginMsg();
        if(loginMsg!= null){
            setMUrl("apps/oa/hysap?date=" + TimeUtils.getToday() + "&uid=" + loginMsg.getUid()+"&pwd="+loginMsg.getPsw());
        }else {
            setMUrl("apps/oa/hysap?date=" + TimeUtils.getToday() );
        }
    }

    public void setMUrl(String url){
        mError.setErrorType(ErrorLayout.LOADDATA);
        if(DeviceUtil.checkNet()){
            setWebView();
            url = getUrl(url);
            Logger.info("##########murl=" + url);
            mWebView.removeAllViews();
            mWebView.clearCache(true);
            mWebView.loadUrl(url);
            Logger.info("##########mCount=" + mWebView.getChildCount());
        }else {
            if(mError != null) {
                mError.setErrorType(ErrorLayout.NETWORK_ERROR);
            }
        }
    }


    private void initCustomTimePicker() {
        // 注意，自定义布局中，optionspicker 或者 timepicker 的布局必须要有（即WheelView内容部分）
        // 否则会报空指针
        // 具体可参考demo 里面的两个自定义布局

        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(TimeUtils.getCurrYear()-5,1,1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(TimeUtils.getCurrYear()+5,12,31);
        //时间选择器 ，自定义布局
        pvCustomTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                String time = TimeUtils.getDay(date);
                setTitle(time+" "+ TimeUtils.getWeekDayInt2Str(TimeUtils.getWeek(time)));
                LoginMsg loginMsg = getSharedPreferences().getLoginMsg();
                if(loginMsg!= null){
                    setMUrl("apps/oa/hysap?date=" + time + "&uid=" + loginMsg.getUid()+"&pwd="+loginMsg.getPsw());
                }else {
                    setMUrl("apps/oa/hysap?date=" + time );
                }
//                setMUrl("apps/oa/hysap?date="+time);
            }
        })      .setType(TimePickerView.Type.YEAR_MONTH_DAY)
                .setDate(selectedDate)
                .setRangDate(startDate,endDate)
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        TextView ivCancel = (TextView) v.findViewById(R.id.tv_cancle);
                        TextView tv_time = (TextView) v.findViewById(R.id.tv_time);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.returnData(tvSubmit);
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.dismiss();
                            }
                        });
                    }
                })
                .build();
    }

    private void setWebView ( ) {
        WebSettings settings = mWebView.getSettings ( );
        mWebView.setWebViewClient ( new MyWebViewClient (  ) );
        mWebView.setWebChromeClient ( new MyWebChromeClient (  ) );
        settings.setSupportZoom ( true );          //支持缩放
        settings.setBlockNetworkImage ( false );  //设置图片最后加载
        settings.setDatabaseEnabled ( true );
        settings.setCacheMode ( WebSettings.LOAD_NO_CACHE );
        settings.setJavaScriptEnabled ( true );    //启用JS脚本
    }

    public class MyWebViewClient extends WebViewClient {
        public MyWebViewClient ( ) {
            super ( );
        }



        @Override
        public void onPageStarted ( WebView view, String url, Bitmap favicon ) {
            super.onPageStarted ( view, url, favicon );
        }

        @Override
        public void onPageFinished ( WebView webView, String url ) {
            super.onPageFinished ( webView, url );
            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
        }
    }

    public class MyWebChromeClient extends WebChromeClient {

        public MyWebChromeClient ()  {
        }

        @Override
        public void onProgressChanged ( WebView view, int newProgress ) {
            Logger.info("##########newProgress="+newProgress);
            if(newProgress>=100){
                mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
            }
        }

        @Override
        public void onReceivedTitle ( WebView view, String title ) {
            super.onReceivedTitle ( view, title );
        }

    }


    /**
     * 处理url
     * @param url
     * @return
     */
    private String getUrl(String url) {
        if (url.startsWith("http://") || url.startsWith("https://")) {
            return url;
        } else {
            return getReString(R.string.rootUrl)+url;
        }
    }




}
