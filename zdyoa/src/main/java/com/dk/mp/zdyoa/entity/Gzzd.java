package com.dk.mp.zdyoa.entity;

/**
 * 规章制度实体
 * 作者：janabo on 2017/3/17 09:35
 */
public class Gzzd {
    private String id;// id
    private String content ;// 内容
    private String date ;// 时间
    private String url ;// 链接
    private String user ;// 作者
    private String title ;//标题
    private String bm;// 部门
    private String lx ;// 类型
    private String titleSec;
    private String subTitle;//附标题
    private String fj;
    private String time; //通知公告time

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBm() {
        return bm;
    }

    public void setBm(String bm) {
        this.bm = bm;
    }

    public String getLx() {
        return lx;
    }

    public void setLx(String lx) {
        this.lx = lx;
    }

    public String getTitleSec() {
        return titleSec;
    }

    public void setTitleSec(String titleSec) {
        this.titleSec = titleSec;
    }

    public String getFj() {
        return fj;
    }

    public void setFj(String fj) {
        this.fj = fj;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
