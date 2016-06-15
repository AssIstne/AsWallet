package com.assistne.aswallet.home;

import android.support.annotation.NonNull;

import com.assistne.aswallet.database.bean.Bill;
import com.assistne.aswallet.database.dao.BillDao;
import com.assistne.aswallet.database.dao.BillDaoImpl;
import com.assistne.aswallet.model.BillModel;
import com.assistne.aswallet.model.ModelTool;

import java.util.Calendar;

/**
 * Created by assistne on 15/12/24.
 */
public class HomePresenter implements HomeMvp.Presenter {

    private HomeMvp.View mView;
    private BillDao mBillDao;

    public HomePresenter(@NonNull HomeMvp.View homeActivity) {
        mView = homeActivity;
        mBillDao = new BillDaoImpl();
    }

    @Override
    public void deleteBillFromDataBase(long billId, int position) {
        mBillDao.deleteBill(billId);
    }

    @Override
    public void softDeleteBill(long billId, int position) {
        if (mBillDao.softDeleteBill(billId)) {
            mView.removeBillFromList(position);
            updateMonthDetail();
        }
    }

    @Override
    public void restoreBill(BillModel billModel, int position) {
        if (mBillDao.restoreBill(billModel.getId())) {
            mView.insertBillToList(position, billModel);
            updateMonthDetail();
        }
    }

    @Override
    public BillModel getBill(int id) {
        return ModelTool.convert(mBillDao.getBill(id));
    }

    @Override
    public void onResume() {
        mView.showBill(ModelTool.convertBillList(mBillDao.getBillList(10)));
        updateMonthDetail();
    }

    // TODO: 16/6/15 删除恢复都重新获取, 可以优化
    /** 更新Drawer中的月总支出收入 */
    private void updateMonthDetail() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        float income = 0;
        float expense =0;
        for (Bill bill :mBillDao.getBillListByDate(calendar.getTimeInMillis())) {
            if (bill.isIncome()) {
                income += bill.getPrice();
            } else {
                expense += bill.getPrice();
            }
        }
        mView.showShortAnalyze(expense, income);
    }
}
