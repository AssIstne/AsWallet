package com.assistne.aswallet.billdetail;

import com.assistne.aswallet.database.dao.BillDao;
import com.assistne.aswallet.database.dao.BillDapImpl;
import com.assistne.aswallet.model.BillModel;
import com.orhanobut.logger.Logger;


/**
 * Presenter of BillDetailActivity
 * Created by assistne on 15/12/27.
 */
public class BillDetailPresenter implements BillMvp.Presenter{
    private BillMvp.View mView;
    private BillDao mBillDao;

    public BillDetailPresenter(BillMvp.View view) {
        mView = view;
        mBillDao = new BillDapImpl();
    }

    @Override
    public void updateBill(BillModel bill) {
        Logger.d(bill.toString());
        mView.exit();
    }

    @Override
    public void getCategory() {

    }

    @Override
    public void getCategory(int id) {

    }

    @Override
    public BillModel getBill(int id) {
        return null;
    }
}
