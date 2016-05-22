package com.assistne.aswallet.home;

import android.support.annotation.NonNull;

import com.assistne.aswallet.database.dao.BillDao;
import com.assistne.aswallet.database.dao.BillDapImpl;
import com.assistne.aswallet.database.bean.Bill;
import com.assistne.aswallet.model.BillModel;
import com.assistne.aswallet.model.ModelTool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by assistne on 15/12/24.
 */
public class HomePresenter implements HomeMvp.Presenter {

    private HomeMvp.View mView;
    private BillDao mBillDao;

    public HomePresenter(@NonNull HomeMvp.View homeActivity) {
        mView = homeActivity;
        mBillDao = new BillDapImpl();
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
        List<BillModel> billModelList = new ArrayList<>();
        for (Bill bill : mBillDao.getBillList(10)) {
            billModelList.add(ModelTool.convert(bill));
        }
        mView.showBill(billModelList);
    }
}
