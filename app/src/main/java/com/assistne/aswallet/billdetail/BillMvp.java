package com.assistne.aswallet.billdetail;

import com.assistne.aswallet.model.BillModel;
import com.assistne.aswallet.model.CategoryModel;
import com.assistne.aswallet.model.TagModel;

import java.util.List;

/**
 * Created by assistne on 16/5/19.
 */
public interface BillMvp {
    interface View {
        void exit();
        void showCategory(List<CategoryModel> categoryList);
        void selectCategory(long categoryId);
        void showTag(List<TagModel> tagList);
    }

    interface Presenter {
        void updateBill(BillModel bill);
        void getCategory();

        CategoryModel getDefaultCategory();

        CategoryModel getCategory(long id);
        BillModel getBill(long id);

        void getTag(int type);
    }
}
