package com.assistne.aswallet.billdetail;

import com.assistne.aswallet.model.Bill;
import com.assistne.aswallet.model.Category;

import java.util.List;

/**
 * Created by assistne on 16/5/19.
 */
public class BillMvp {
    interface View {
        void exit();
        void showCategory(List<Category> categoryList);
        void selectCategory(Category category);
    }

    interface Presenter {
        void updateBill(Bill bill);
        void getCategory();
        void getCategory(int id);
    }
}
