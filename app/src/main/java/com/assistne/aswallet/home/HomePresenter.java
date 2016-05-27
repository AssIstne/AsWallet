package com.assistne.aswallet.home;

import android.support.annotation.NonNull;

import com.assistne.aswallet.database.dao.BillDao;
import com.assistne.aswallet.database.dao.BillDaoImpl;
import com.assistne.aswallet.model.BillModel;
import com.assistne.aswallet.model.ModelTool;

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
    }
}
