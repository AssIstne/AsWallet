package com.assistne.aswallet.home;

import android.support.annotation.NonNull;

import com.assistne.aswallet.model.Bill;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by assistne on 15/12/24.
 */
public class HomePresenter implements HomeMvp.Presenter {

    private HomeMvp.View mView;


    public HomePresenter(@NonNull HomeMvp.View homeActivity) {
        mView = homeActivity;
    }

    @Override
    public void deleteBillFromDataBase() {

    }

    @Override
    public Bill getBill(int id) {
        return DataSupport.find(Bill.class, id);
    }

    @Override
    public void onResume() {
        List<Bill> data = DataSupport
                .limit(10)
                .order("dateinmills desc")
                .find(Bill.class);
        mView.showBill(data);
    }
}
