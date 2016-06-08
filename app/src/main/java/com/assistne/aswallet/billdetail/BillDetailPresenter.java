package com.assistne.aswallet.billdetail;

import com.assistne.aswallet.database.PrimaryKeyFactory;
import com.assistne.aswallet.database.bean.Bill;
import com.assistne.aswallet.database.bean.Category;
import com.assistne.aswallet.database.dao.BillDao;
import com.assistne.aswallet.database.dao.BillDaoImpl;
import com.assistne.aswallet.database.dao.CategoryDao;
import com.assistne.aswallet.database.dao.CategoryDaoImpl;
import com.assistne.aswallet.database.dao.TagDao;
import com.assistne.aswallet.database.dao.TagDaoImpl;
import com.assistne.aswallet.model.BillModel;
import com.assistne.aswallet.model.CategoryModel;
import com.assistne.aswallet.model.ModelTool;
import com.orhanobut.logger.Logger;

import java.util.Date;


/**
 * Presenter of BillDetailActivity
 * Created by assistne on 15/12/27.
 */
public class BillDetailPresenter implements BillMvp.Presenter{
    private BillMvp.View mView;
    private BillDao mBillDao;
    private CategoryDao mCategoryDao;
    private TagDao mTagDao;

    public BillDetailPresenter(BillMvp.View view) {
        mView = view;
        mBillDao = new BillDaoImpl();
        mCategoryDao = new CategoryDaoImpl();
        mTagDao = new TagDaoImpl();
    }

    @Override
    public void updateBill(BillModel billModel) {
        Logger.d(billModel.toString());
        long categoryId = billModel.getCategoryId();
        /** 手动计数 */
        Category category = mCategoryDao.increaseCategory(categoryId);
        Bill bill = ModelTool.convert(billModel, category, mTagDao.getTag(billModel.getTagId()));
        if (bill.getId() == -1) {
            bill.setId(PrimaryKeyFactory.nextBillKey());
            /** 新增时, 记录保存时的时间 */
            bill.setDate(new Date());
        }
        Logger.d(bill.toString());
        mBillDao.updateBill(bill);
        mView.exit();
    }

    @Override
    public void getCategory() {
        mView.showCategory(ModelTool.convertCategoryList(mCategoryDao.getExpenseCategoryList(0)));
    }

    @Override
    public CategoryModel getDefaultCategory() {
        return ModelTool.convert(mCategoryDao.getDefaultCategory());
    }

    @Override
    public CategoryModel getCategory(long id) {
        return ModelTool.convert(mCategoryDao.getCategory(id));
    }

    @Override
    public BillModel getBill(long id) {
        return ModelTool.convert(mBillDao.getBill(id));
    }

    @Override
    public void getTag(int type) {
        if (type == Category.TYPE_INCOME) {
            mView.showTag(ModelTool.convertTagList(mTagDao.getIncomeTagList(-1)));
        } else {
            mView.showTag(ModelTool.convertTagList(mTagDao.getExpenseTagList(-1)));
        }
    }
}
