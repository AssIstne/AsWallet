package com.assistne.aswallet.database.dao;

import com.assistne.aswallet.database.bean.Bill;

import java.util.List;

import io.realm.Realm;
import io.realm.Sort;

/**
 * Created by assistne on 16/5/21.
 */
public class BillDapImpl implements BillDao {
    @Override
    public Bill getBill(long id) {
        Realm realm = Realm.getDefaultInstance();
        Bill res = realm.where(Bill.class).equalTo(Bill.Structure.ID, id).findFirst();
        realm.close();
        return res;
    }

    @Override
    public void updateBill(Bill billInput) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(billInput);
        realm.commitTransaction();
        realm.close();
    }

    @Override
    public List<Bill> getBillList(int count) {
        Realm realm = Realm.getDefaultInstance();
        List<Bill> res = realm.where(Bill.class).findAllSorted(Bill.Structure.ID, Sort.DESCENDING);
        int size = res.size();
        if (size > 10) {
            res = realm.where(Bill.class).greaterThanOrEqualTo(Bill.Structure.ID, size - 10)
                    .findAllSorted(Bill.Structure.ID, Sort.DESCENDING);
        }
        realm.close();
        return res;
    }
}
