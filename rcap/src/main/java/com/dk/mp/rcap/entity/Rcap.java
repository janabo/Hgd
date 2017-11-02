package com.dk.mp.rcap.entity;

import java.io.Serializable;

/**
 * 作者：janabo on 2017/3/22 17:55
 */
public class Rcap implements Serializable{
    private String location;//订单
    private String content;//内容
    private String title;//标题
    private String mentionTime;//提醒时间
    private String attendLeader;//与会领导
    private String scheduleTypedm;//部门
    private String time_start;//开始时间
    private String time_end;//结束时间

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMentionTime() {
        return mentionTime;
    }

    public void setMentionTime(String mentionTime) {
        this.mentionTime = mentionTime;
    }

    public String getAttendLeader() {
        return attendLeader;
    }

    public void setAttendLeader(String attendLeader) {
        this.attendLeader = attendLeader;
    }

    public String getScheduleTypedm() {
        return scheduleTypedm;
    }

    public void setScheduleTypedm(String scheduleTypedm) {
        this.scheduleTypedm = scheduleTypedm;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }
}
