package com.assistne.aswallet.database.dao;

import com.assistne.aswallet.database.RealmDelegate;
import com.assistne.aswallet.database.bean.Bill;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.Sort;

/**
 * Created by assistne on 16/5/21.
 */
public class BillDaoImpl implements BillDao {
    @Override
    public Bill getBill(long id) {
        Realm realm = RealmDelegate.getInstance();
        return realm.where(Bill.class).equalTo(Bill.Structure.ID, id).findFirst();
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
        if (count > 0) {
            int size = res.size();
            if (size > count) {
                res = realm.where(Bill.class).greaterThanOrEqualTo(Bill.Structure.ID, size - count)
                        .findAllSorted(Bill.Structure.ID, Sort.DESCENDING);
            }
        }
        return res;
    }

    @Override
    public List<Bill> getBillListByDate(long from) {
        return getBillListByDate(from, Calendar.getInstance().getTimeInMillis());
    }

    @Override
    public List<Bill> getBillListByDate(long from, long to) {
        if (to < from) {
            return null;
        }
        Realm realm = RealmDelegate.getInstance();
        return realm.where(Bill.class)
                .greaterThan(Bill.Structure.DATE, new Date(from))
                .lessThan(Bill.Structure.DATE, new Date(to)).findAllSorted(Bill.Structure.ID, Sort.DESCENDING);
    }
}
