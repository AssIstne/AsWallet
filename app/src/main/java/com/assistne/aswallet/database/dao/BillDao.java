package com.assistne.aswallet.database.dao;

import com.assistne.aswallet.database.bean.Bill;

import java.util.List;

/**
 * 从数据库获取{@link Bill}数据
 * Created by assistne on 16/5/21.
 */
public interface BillDao {
    Bill getBill(long id);
    void updateBill(Bill billInput);
    List<Bill> getBillList(int count);
}
