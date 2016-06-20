package com.assistne.aswallet.category;

import android.util.SparseLongArray;

import com.assistne.aswallet.database.dao.CategoryDao;
import com.assistne.aswallet.database.dao.CategoryDaoImpl;
import com.assistne.aswallet.database.dao.TagDao;
import com.assistne.aswallet.database.dao.TagDaoImpl;
import com.assistne.aswallet.model.CategoryModel;
import com.assistne.aswallet.model.ModelTool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by assistne on 16/6/16.
 */
public class EditCatPresenter implements EditCatMvp.Presenter {

    private EditCatMvp.View mView;
    private CategoryDao mCatDao;
    private TagDao mTagDao;

    public EditCatPresenter(EditCatMvp.View view) {
        mView = view;
        mCatDao = new CategoryDaoImpl();
        mTagDao = new TagDaoImpl();
    }

    @Override
    public void addOrUpdateCategory(CategoryModel categoryModel) {
        mCatDao.updateCategory(ModelTool.convert(categoryModel));
        mView.insertCategory(categoryModel);
    }

    @Override
    public void deleteCategory(long catId, int position) {
        if (catId == -1 && position == -1) {
            mCatDao.deleteCategory(-1);
            return;
        }
        throw new RuntimeException("Delete Nothing here!");
    }

    @Override
    public void softDeleteCategory(SparseLongArray array) {
        List<Integer> resArray = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            int position = array.keyAt(i);
            if (mCatDao.softDeleteCategory(array.valueAt(i))) {
                resArray.add(position);
            }
        }
        mView.removeCategory(resArray);
    }

    @Override
    public void requestShowTags(long catId, int position) {
        mView.showTagList(ModelTool.convertTagList(mTagDao.getTagListByCatId(catId)), position);
    }

    @Override
    public void onResume() {
        mView.showCategoryList(ModelTool.convertCategoryList(mCatDao.getExpenseCategoryListIdDesc(-1)));
    }
}
