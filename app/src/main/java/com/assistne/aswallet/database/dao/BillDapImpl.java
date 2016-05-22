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
    public List<Bill> getBillList(int count) {
        Realm realm = Realm.getDefaultInstance();
        long startId = (realm.where(Bill.class).count() - count);
        List<Bill> res = realm.where(Bill.class)
                .greaterThanOrEqualTo(Bill.Structure.ID, startId)
                .findAllSorted(Bill.Structure.DATE, Sort.DESCENDING);
        realm.close();
        return res;
    }
}
