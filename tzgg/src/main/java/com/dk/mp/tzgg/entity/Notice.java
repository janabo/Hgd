package com.dk.mp.tzgg.entity;

import java.util.List;

/**
 * 作者：janabo on 2017/10/26 10:12
 */
public class Notice {
    private String id;//id
    private String title;//标题
    private String desc;//简介
    private String author;//作者
    private String content;//内容
    private String publishTime;//发布时间
    private String loadTime;//加载时间
    private int status;//0：已读，1：未读，2：最新
    private String url;//
    private List<Fujian> fjs;//附件列表

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getLoadTime() {
        return loadTime;
    }

    public void setLoadTime(String loadTime) {
        this.loadTime = loadTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Fujian> getFjs() {
        return fjs;
    }

    public void setFjs(List<Fujian> fjs) {
        this.fjs = fjs;
    }
}
