package com.assistne.aswallet.database.dao;

import com.assistne.aswallet.database.RealmDelegate;
import com.assistne.aswallet.database.bean.Category;
import com.orhanobut.logger.Logger;

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
    public List<Category> getCategoryList(int count) {
        Realm realm = RealmDelegate.getInstance();
        List<Category> res = realm.where(Category.class).findAllSorted(Category.Structure.ID, Sort.ASCENDING);
        if (count > 0) {
            int size = res.size();
            if (size > count) {
                res = realm.where(Category.class).greaterThanOrEqualTo(Category.Structure.ID, size - count)
                        .findAllSorted(Category.Structure.ID, Sort.ASCENDING);
            }
        }
        return res;
    }

    @Override
    public Category getDefaultCategory() {
        return getCategoryList(0).get(0);
    }
}
