package com.dk.mp.rcap.entity;

import java.util.List;

/**
 * 作者：janabo on 2017/3/23 11:19
 */
public class Rc {
    private List<String> dates;
    private List<Rcap> list;

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    public List<Rcap> getList() {
        return list;
    }

    public void setList(List<Rcap> list) {
        this.list = list;
    }
}
