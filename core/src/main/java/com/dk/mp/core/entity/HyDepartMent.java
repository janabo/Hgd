package com.dk.mp.core.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 作者：janabo on 2017/10/26 17:47
 */
public class HyDepartMent extends RealmObject {
    @PrimaryKey
    private String id;//id
    private String name;//部门,姓名


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
}
