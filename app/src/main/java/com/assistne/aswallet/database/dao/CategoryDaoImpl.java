package com.assistne.aswallet.database.dao;

import com.assistne.aswallet.database.bean.Category;

import io.realm.Realm;

/**
 * Created by assistne on 16/5/21.
 */
public class CategoryDaoImpl implements CategoryDao {
    @Override
    public Category getCategory(long id) {
        Realm realm = Realm.getDefaultInstance();
        Category category = realm.where(Category.class).equalTo(Category.Structure.ID, id).findFirst();
        realm.close();
        return category;
    }
}
