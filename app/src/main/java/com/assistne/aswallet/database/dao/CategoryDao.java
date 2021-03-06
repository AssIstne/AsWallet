package com.assistne.aswallet.database.dao;

import com.assistne.aswallet.database.bean.Category;

import java.util.List;

/**
 * 从数据库获取{@link com.assistne.aswallet.database.bean.Category}数据
 * Created by assistne on 16/5/21.
 */
public interface CategoryDao {
    Category getCategory(long id);
    /**
     * @param count 取出的数量, -1表示全部*/
    List<Category> getExpenseCategoryList(int count);
    /**
    * @param count 取出的数量, -1表示全部*/
    List<Category> getExpenseCategoryListIdDesc(int count);

    Category getDefaultCategory();
    /** 手动增加{@link com.assistne.aswallet.database.bean.Category.Structure#COUNT} */
    Category increaseCategory(long id);

    boolean deleteCategory(long id);
    boolean softDeleteCategory(long id);
    void updateCategory(Category category);

}
