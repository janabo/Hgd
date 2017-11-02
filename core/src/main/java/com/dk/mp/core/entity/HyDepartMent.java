package com.dk.mp.core.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 作者：janabo on 2017/10/26 17:47
 */
public class HyDepartMent extends RealmObject {
    @PrimaryKey
    private String idDepart;//id
    private String nameDepart;//部门,姓名

    public String getIdDepart() {
        return idDepart;
    }

    public void setIdDepart(String idDepart) {
        this.idDepart = idDepart;
    }

    public String getNameDepart() {
        return nameDepart;
    }

    public void setNameDepart(String nameDepart) {
        this.nameDepart = nameDepart;
    }
}
