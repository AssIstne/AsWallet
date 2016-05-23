package com.assistne.aswallet.billdetail;

import com.assistne.aswallet.database.bean.Bill;
import com.assistne.aswallet.database.bean.Category;
import com.assistne.aswallet.model.BillModel;
import com.assistne.aswallet.model.CategoryModel;

import java.util.List;

/**
 * Created by assistne on 16/5/19.
 */
public class BillMvp {
    interface View {
        void exit();
        void showCategory(List<CategoryModel> categoryList);
        void selectCategory(CategoryModel category);
    }

    interface Presenter {
        void updateBill(BillModel bill);
        void getCategory();
        void getCategory(long id);
        BillModel getBill(long id);
    }
}
