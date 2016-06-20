package com.assistne.aswallet.category;

import android.support.annotation.NonNull;
import android.util.SparseLongArray;

import com.assistne.aswallet.component.BaseMvp;
import com.assistne.aswallet.model.CategoryModel;
import com.assistne.aswallet.model.TagModel;

import java.util.List;

/**
 * Created by assistne on 16/6/16.
 */
public interface EditCatMvp {
    interface View {
        void showCategoryList(List<CategoryModel> catList);
        void insertCategory(CategoryModel categoryModel);
        void removeCategory(@NonNull List<Integer> positionList);
        void showTagList(List<TagModel> tagList, int position);
    }

    interface Presenter extends BaseMvp.Presenter {
        void addOrUpdateCategory(CategoryModel categoryModel);
        void deleteCategory(long catId, int position);
        /**
         * 批量删除类别
         * @param array key是position, value是CategoryId*/
        void softDeleteCategory(SparseLongArray array);
        void requestShowTags(long catId, int position);
    }
}
