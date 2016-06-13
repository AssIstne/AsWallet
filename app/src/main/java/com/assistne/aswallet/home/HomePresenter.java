package com.assistne.aswallet.home;

import android.support.annotation.NonNull;

import com.assistne.aswallet.database.bean.Bill;
import com.assistne.aswallet.database.dao.BillDao;
import com.assistne.aswallet.database.dao.BillDaoImpl;
import com.assistne.aswallet.model.BillModel;
import com.assistne.aswallet.model.ModelTool;
import com.orhanobut.logger.Logger;

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
    public void deleteBillFromDataBase() {

    }

    @Override
    public BillModel getBill(int id) {
        return ModelTool.convert(mBillDao.getBill(id));
    }

    @Override
    public void onResume() {
        mView.showBill(ModelTool.convertBillList(mBillDao.getBillList(10)));
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
