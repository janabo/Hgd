package com.dk.mp.rcap;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.android.volley.VolleyError;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.TimeUtils;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.rcap.adapter.RcapAdapter;
import com.dk.mp.rcap.entity.Rc;
import com.dk.mp.rcap.entity.Rcap;
import com.dk.mp.rcap.manager.CalendarManager;
import com.dk.mp.rcap.widget.CollapseCalendarView;

import org.joda.time.LocalDate;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：janabo on 2017/3/22 17:08
 */
public class RcapHomeActivity extends MyActivity {
    private CollapseCalendarView calendarView;
    private CalendarManager mManager;
    private SimpleDateFormat sdf;
    private RecyclerView mRecyclerView;
    private RcapAdapter mAdapter;
    private String mDate= TimeUtils.getToday();
    private List<String> rqs = new ArrayList<>();//当月有日程的日期
    private List<Rcap> rcaps = new ArrayList<>();//当天的日程
    private Map<String,String> rqMap = new HashMap<>();
    private ErrorLayout error_layout;


    @Override
    protected int getLayoutID() {
        return R.layout.app_rcap_home;
    }

    @Override
    protected void initView() {
        super.initView();
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, com.dk.mp.core.R.color.colorPrimary));
        }
    }

    @Override
    protected void initialize() {
        super.initialize();
        setTitle(TimeUtils.getToday2());
        sdf = new SimpleDateFormat("yyyy.MM.dd");
        error_layout = (ErrorLayout) findViewById(R.id.error_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        mRecyclerView.setHasFixedSize ( true );
        mRecyclerView.setLayoutManager ( new LinearLayoutManager( mContext ) );
        mAdapter = new RcapAdapter(mContext,rcaps,TimeUtils.getToday());
        mRecyclerView.setAdapter ( mAdapter );
        mRecyclerView.setNestedScrollingEnabled(false);
        calendarView = (CollapseCalendarView) findViewById(R.id.calendar);
        calendarView.showChinaDay(false);
        calendarView.hideHeader();
        mManager = new CalendarManager(LocalDate.now(),
                CalendarManager.State.MONTH, LocalDate.now().withYear(100),
                LocalDate.now().plusYears(100));
        /**
         * 日期选中监听器
         */
        calendarView.setDateSelectListener(new CollapseCalendarView.OnDateSelect() {

            @Override
            public void onDateSelected(LocalDate date) {
                setTitle(TimeUtils.formatDateTime(date.toString()));
                if(!mDate.equals( date.toString())) {
                    mDate = date.toString();
                    if(mAdapter!= null) {
                        mAdapter.setDate(mDate);
                    }
                    if(DeviceUtil.checkNet()) {
                        error_layout.setErrorType(ErrorLayout.LOADDATA);
                        getRcaps(mDate);
                    }else{
                        mRecyclerView.setVisibility(View.GONE);
                        error_layout.setErrorType(ErrorLayout.NETWORK_ERROR);
                    }
                }
            }
        });
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, 9);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        try {
            for (int i = 0; i < 30; i++) {
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //初始化日历管理器
        calendarView.init(mManager,rqMap);
        if(DeviceUtil.checkNet()) {
            getRcaps(TimeUtils.getToday());
        }else{
            mRecyclerView.setVisibility(View.GONE);
            error_layout.setErrorType(ErrorLayout.NETWORK_ERROR);
        }
    }

    public void getRcaps(String day){
        Map<String,Object> map = new HashMap<>();
        map.put("date",day);
        HttpUtil.getInstance().postJsonObjectRequest("apps/oa/rcap", map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result)  {
                try {
                    if (result.getInt("code") == 200) {
                        JSONObject jsonObject = result.getJSONObject("data");
                        Rc rc = getGson().fromJson(jsonObject.toString(),Rc.class);
                        rcaps.clear();
                        if(rc.getList().size()>0){
                            mRecyclerView.setVisibility(View.VISIBLE);
                            error_layout.setErrorType(ErrorLayout.HIDE_LAYOUT);
                            rcaps.addAll(rc.getList());
                            mAdapter.notifyDataSetChanged();
                        }else{
                            mRecyclerView.setVisibility(View.GONE);
                            error_layout.setErrorType(ErrorLayout.NODATA);
                        }
                        rqs.clear();
                        rqs.addAll(rc.getDates());
                        rqMap.clear();
                        for(String r :rqs){
                           rqMap.put(r,r);
                        }
                        calendarView.init(mManager,rqMap);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    mRecyclerView.setVisibility(View.GONE);
                    error_layout.setErrorType(ErrorLayout.DATAFAIL);
                }
            }
            @Override
            public void onError(VolleyError error) {
                mRecyclerView.setVisibility(View.GONE);
                error_layout.setErrorType(ErrorLayout.DATAFAIL);
            }
        });
    }

}
