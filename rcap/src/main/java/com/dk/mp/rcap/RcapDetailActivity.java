package com.dk.mp.rcap;

import android.widget.TextView;

import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.rcap.entity.Rcap;


/**
 * 作者：janabo on 2016/12/28 14:57
 */
public class RcapDetailActivity extends MyActivity{
    private TextView title, content, place;
    private TextView starttime, endtime;
    private Rcap rcap;


    @Override
    protected int getLayoutID() {
        return R.layout.app_rcap_detail;
    }

    @Override
    protected void initialize() {
        super.initialize();
        setTitle(getIntent().getStringExtra("title"));
        initView();
    }

    public void initView(){
        title = (TextView) findViewById(R.id.schedule_title);
        content = (TextView) findViewById(R.id.schedule_content);
        place = (TextView) findViewById(R.id.schedule_place);
        starttime = (TextView) findViewById(R.id.show_starttime);
        endtime = (TextView) findViewById(R.id.show_endtime);

        rcap = (Rcap) getIntent().getSerializableExtra("rcapDetail");
        title.setText(rcap.getTitle());
        content.setText(rcap.getContent());
        place.setText(rcap.getLocation());
        starttime.setText(rcap.getTime_start());
        endtime.setText(rcap.getTime_end());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
