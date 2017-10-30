package com.dk.mp.gzcx.entity;

/**
 * 工资查询实体
 * 作者：janabo on 2017/10/24 11:58
 */
public class Salary {
    private String id;
    private String title;//标题
    private String rq;//日期

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

    public String getRq() {
        return rq;
    }

    public void setRq(String rq) {
        this.rq = rq;
    }
}
