package com.assistne.aswallet.billdetail;

import com.assistne.aswallet.database.bean.Bill;
import com.assistne.aswallet.database.bean.Category;
import com.assistne.aswallet.database.dao.BillDao;
import com.assistne.aswallet.database.dao.BillDapImpl;
import com.assistne.aswallet.database.dao.CategoryDao;
import com.assistne.aswallet.database.dao.CategoryDaoImpl;
import com.assistne.aswallet.model.BillModel;
import com.assistne.aswallet.model.ModelTool;
import com.orhanobut.logger.Logger;


/**
 * Presenter of BillDetailActivity
 * Created by assistne on 15/12/27.
 */
public class BillDetailPresenter implements BillMvp.Presenter{
    private BillMvp.View mView;
    private BillDao mBillDao;
    private CategoryDao mCategoryDao;

    public BillDetailPresenter(BillMvp.View view) {
        mView = view;
        mBillDao = new BillDapImpl();
        mCategoryDao = new CategoryDaoImpl();
    }

    @Override
    public void updateBill(BillModel bill) {
        Logger.d(bill.toString());
        long categoryId = bill.getCategoryId();
        mBillDao.updateBill(ModelTool.convert(bill, mCategoryDao.getCategory(categoryId)));
        mView.exit();
    }

    @Override
    public void getCategory() {

    }

    @Override
    public void getCategory(long id) {

    }

    @Override
    public BillModel getBill(long id) {
        return ModelTool.convert(mBillDao.getBill(id));
    }
}
