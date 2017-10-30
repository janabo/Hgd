package com.dk.mp.hy.db;

import android.content.Context;

import com.dk.mp.core.entity.HyDepartMent;
import com.dk.mp.core.entity.HyJbxx;
import com.dk.mp.core.entity.HyXbPersons;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * 通讯录
 * 作者：janabo on 2016/12/23 10:46
 */
public class RealmHelper {

    private Realm mRealm;

    public RealmHelper(Context context) {
        mRealm = Realm.getDefaultInstance();
    }


    /**
     * 批量新增部门
     * @param d
     */
    public void addDepartment(final List<HyDepartMent> d){
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(d);
        mRealm.commitTransaction();
    }

    /**
     * 新增星标同事
     * @return
     */
    public List<HyXbPersons> queryALlXb(){
        RealmResults<HyXbPersons> jbxxs = mRealm.where(HyXbPersons.class).findAll();
        return mRealm.copyFromRealm(jbxxs);
    }

    /**
     * 新增部门
     * @return
     */
    public List<HyDepartMent> queryAllDepartment(){
        RealmResults<HyDepartMent> jbxxs = mRealm.where(HyDepartMent.class).findAll();
        return mRealm.copyFromRealm(jbxxs);
    }

    /**
     * delete部门 （删）
     */
    public void deleteDepartment() {
        RealmResults<HyDepartMent> d = mRealm.where(HyDepartMent.class).findAll();
        mRealm.beginTransaction();
        d.deleteAllFromRealm();
        mRealm.commitTransaction();
    }

    /**
     * delete星标同事 （删）
     */
    public void deleteALlXb() {
        RealmResults<HyJbxx> d = mRealm.where(HyJbxx.class).findAll();
        mRealm.beginTransaction();
        d.deleteAllFromRealm();
        mRealm.commitTransaction();
    }


    /**
     * 批量新增部门
     * @param d
     */
    public void addPersons(final List<HyJbxx> d){
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(d);
        mRealm.commitTransaction();
    }

    /**
     * delete指定部门下的人员（删）
     */
    public void deletePersonsByDepartmentid(String departmentid) {
        RealmResults<HyJbxx> d = mRealm.where(HyJbxx.class).equalTo("departmentid",departmentid).findAll();
        mRealm.beginTransaction();
        d.deleteAllFromRealm();
        mRealm.commitTransaction();
    }

    /**
     * 查询指定部门下人员
     * @return
     */
    public List<HyJbxx> queryPersonsByDepartmentid(String departmentid){
        RealmResults<HyJbxx> jbxxs = mRealm.where(HyJbxx.class).equalTo("departmentid",departmentid).findAll();
        return mRealm.copyFromRealm(jbxxs);
    }

    /**
     * 通过主键查询数据
     * @param key 主键
     * @return
     */
    public HyXbPersons queryPersonsByKey(String key){
        HyXbPersons j=mRealm.where(HyXbPersons.class).equalTo("prikey",key).findFirst();
        return j;
    }

    /**
     * 删除指定的星标用户
     * @param prikey
     */
    public void deleteXbById(String prikey){
        HyXbPersons d = mRealm.where(HyXbPersons.class).equalTo("prikey", prikey).findFirst();
        mRealm.beginTransaction();
        d.deleteFromRealm();
        mRealm.commitTransaction();
    }
    /**
     * 新增星标同事
     * @param j
     */
    public void addXb(final HyXbPersons j){
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(j);
        mRealm.commitTransaction();
    }

}
