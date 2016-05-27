package com.assistne.aswallet.billdetail;

import com.assistne.aswallet.database.PrimaryKeyFactory;
import com.assistne.aswallet.database.bean.Bill;
import com.assistne.aswallet.database.dao.BillDao;
import com.assistne.aswallet.database.dao.BillDaoImpl;
import com.assistne.aswallet.database.dao.CategoryDao;
import com.assistne.aswallet.database.dao.CategoryDaoImpl;
import com.assistne.aswallet.model.BillModel;
import com.assistne.aswallet.model.CategoryModel;
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
        mBillDao = new BillDaoImpl();
        mCategoryDao = new CategoryDaoImpl();
    }

    @Override
    public void updateBill(BillModel billModel) {
        Logger.d(billModel.toString());
        long categoryId = billModel.getCategoryId();
        Bill bill = ModelTool.convert(billModel, mCategoryDao.getCategory(categoryId));
        if (bill.getId() == -1) {
            bill.setId(PrimaryKeyFactory.nextBillKey());
        }
        Logger.d(bill.toString());
        mBillDao.updateBill(bill);
        mView.exit();
    }

    @Override
    public void getCategory() {
        mView.showCategory(ModelTool.convertCategoryList(mCategoryDao.getCategoryList(0)));
    }

    @Override
    public CategoryModel getDefaultCategory() {
        return ModelTool.convert(mCategoryDao.getDefaultCategory());
    }

    @Override
    public void getCategory(long id) {

    }

    @Override
    public BillModel getBill(long id) {
        return ModelTool.convert(mBillDao.getBill(id));
    }
}
