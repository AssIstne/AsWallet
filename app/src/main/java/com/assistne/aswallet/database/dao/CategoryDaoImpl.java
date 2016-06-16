package com.assistne.aswallet.database.dao;

import com.assistne.aswallet.database.RealmDelegate;
import com.assistne.aswallet.database.bean.Category;

import java.util.List;

import io.realm.Realm;
import io.realm.Sort;

/**
 * Created by assistne on 16/5/21.
 */
public class CategoryDaoImpl implements CategoryDao {
    @Override
    public Category getCategory(long id) {
        Realm realm = RealmDelegate.getInstance();
        return realm.where(Category.class).equalTo(Category.Structure.ID, id).findFirst();
    }

    @Override
    public List<Category> getExpenseCategoryList(int count) {
        Realm realm = RealmDelegate.getInstance();
        List<Category> res = realm.where(Category.class).equalTo(Category.Structure.TYPE, Category.TYPE_EXPENSE)
                .notEqualTo(Category.Structure.SOFT_DELETE, true)
                .findAllSorted(Category.Structure.ID, Sort.ASCENDING);
        if (count > 0) {
            int size = res.size();
            if (size > count) {
                res = realm.where(Category.class).greaterThanOrEqualTo(Category.Structure.ID, res.get(size - count).getId())
                        .findAllSorted(Category.Structure.ID, Sort.ASCENDING);
            }
        }
        return res;
    }

    /** 包含账单最多的类别 */
    @Override
    public Category getDefaultCategory() {
        Realm realm = RealmDelegate.getInstance();
        List<Category> res = realm.where(Category.class).equalTo(Category.Structure.TYPE, Category.TYPE_EXPENSE)
                .notEqualTo(Category.Structure.ID, Category.Type.INCOME)
                .notEqualTo(Category.Structure.SOFT_DELETE, true)
                .findAllSorted(Category.Structure.COUNT, Sort.DESCENDING);
        return res.get(0);
    }

    /** 增加账单数量 */
    @Override
    public Category increaseCategory(long id) {
        Realm realm = RealmDelegate.getInstance();
        realm.beginTransaction();
        Category category = realm.where(Category.class).equalTo(Category.Structure.ID, id).findFirst();
        category.increaseCount();
        realm.commitTransaction();
        return category;
    }

    @Override
    public boolean deleteCategory(long id) {
        if (id == -1) {
            Realm realm = RealmDelegate.getInstance();
            realm.beginTransaction();
            for(Category category : realm.where(Category.class).findAll()) {
                category.setSoftDelete(false);
            }
            realm.commitTransaction();
        }
        return false;
    }

    @Override
    public boolean softDeleteCategory(long id) {
        Realm realm = RealmDelegate.getInstance();
        realm.beginTransaction();
        Category category = realm.where(Category.class).equalTo(Category.Structure.ID, id).findFirst();
        category.setSoftDelete(true);
        realm.commitTransaction();
        return true;
    }


}
