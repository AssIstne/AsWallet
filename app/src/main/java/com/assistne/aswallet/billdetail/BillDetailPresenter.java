package com.assistne.aswallet.billdetail;

import com.assistne.aswallet.model.Bill;
import com.assistne.aswallet.model.Category;
import com.orhanobut.logger.Logger;

import org.litepal.crud.DataSupport;

/**
 * Presenter of BillDetailActivity
 * Created by assistne on 15/12/27.
 */
public class BillDetailPresenter implements BillMvp.Presenter{
    private BillMvp.View mView;

    public BillDetailPresenter(BillMvp.View view) {
        mView = view;
    }

    @Override
    public void updateBill(Bill bill) {
        Logger.d(bill.toString());
        if (bill.getId() != 0) {
            bill.update(bill.getId());
            Logger.d(DataSupport.find(Bill.class, bill.getId()).toString());
        } else {
            bill.save();
        }
        mView.exit();
    }

    @Override
    public void getCategory() {
        mView.showCategory(DataSupport.findAll(Category.class));
    }

    @Override
    public void getCategory(int id) {
        mView.selectCategory(DataSupport.find(Category.class, id));
    }
}
