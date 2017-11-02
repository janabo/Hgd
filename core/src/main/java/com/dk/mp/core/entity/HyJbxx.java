package com.dk.mp.core.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 作者：janabo on 2017/10/26 17:52
 */
public class HyJbxx extends RealmObject {
    @PrimaryKey
    private String prikey;//主键
    private String id;//id
    private String name;//姓名
    private String deptId;//部门id
    private String deptName;//部门名称
    private String phones;

    public String getPrikey() {
        return prikey;
    }
    public void setPrikey(String prikey) {
        this.prikey = prikey;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getPhones() {
        return phones;
    }

    public void setPhones(String phones) {
        this.phones = phones;
    }
}
