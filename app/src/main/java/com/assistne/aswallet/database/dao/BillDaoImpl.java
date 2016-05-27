package com.assistne.aswallet.database.dao;

import android.util.Log;

import com.assistne.aswallet.database.RealmDelegate;
import com.assistne.aswallet.database.bean.Bill;
import com.orhanobut.logger.Logger;

import java.util.List;

import io.realm.Realm;
import io.realm.Sort;

/**
 * Created by assistne on 16/5/21.
 */
public class BillDaoImpl implements BillDao {
    @Override
    public Bill getBill(long id) {
        Realm realm = Realm.getDefaultInstance();
        Bill res = realm.where(Bill.class).equalTo(Bill.Structure.ID, id).findFirst();
        realm.close();
        return res;
    }

    @Override
    public void updateBill(Bill billInput) {
        Realm realm = RealmDelegate.getInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(billInput);
        realm.commitTransaction();
    }

    @Override
    public List<Bill> getBillList(int count) {
        Realm realm = RealmDelegate.getInstance();
        List<Bill> res = realm.where(Bill.class).findAllSorted(Bill.Structure.ID, Sort.DESCENDING);
        Logger.d("getBillList: " + res.size());
        if (count > 0) {
            int size = res.size();
            if (size > count) {
                res = realm.where(Bill.class).greaterThanOrEqualTo(Bill.Structure.ID, size - count)
                        .findAllSorted(Bill.Structure.ID, Sort.DESCENDING);
            }
        }
        return res;
    }
}
