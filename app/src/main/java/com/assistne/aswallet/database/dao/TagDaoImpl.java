package com.assistne.aswallet.database.dao;

import com.assistne.aswallet.database.RealmDelegate;
import com.assistne.aswallet.database.bean.Category;
import com.assistne.aswallet.database.bean.Tag;

import java.util.List;

import io.realm.Realm;
import io.realm.Sort;

/**
 * Created by assistne on 16/6/6.
 */
public class TagDaoImpl implements TagDao {

    @Override
    public Tag getTag(long id) {
        Realm realm = RealmDelegate.getInstance();
        return realm.where(Tag.class).equalTo(Tag.Structure.ID, id).findFirst();
    }

    @Override
    public List<Tag> getTagList(int count) {
        Realm realm = RealmDelegate.getInstance();
        List<Tag> res = realm.where(Tag.class)
                .findAllSorted(Tag.Structure.ID, Sort.ASCENDING);
        if (count > 0) {
            int size = res.size();
            if (size > count) {
                res = realm.where(Tag.class).greaterThanOrEqualTo(Tag.Structure.ID, res.get(size - count).getId())
                        .findAllSorted(Tag.Structure.ID, Sort.ASCENDING);
            }
        }
        return res;
    }

    @Override
    public List<Tag> getIncomeTagList(int count) {
        Realm realm = RealmDelegate.getInstance();
        List<Tag> res = realm.where(Tag.class)
                .equalTo(Tag.Structure.CAT + "." + Category.Structure.TYPE, Category.TYPE_INCOME)
                .findAllSorted(Tag.Structure.ID, Sort.ASCENDING);
        if (count > 0) {
            int size = res.size();
            if (size > count) {
                res = realm.where(Tag.class).greaterThanOrEqualTo(Tag.Structure.ID, res.get(size - count).getId())
                        .findAllSorted(Tag.Structure.ID, Sort.ASCENDING);
            }
        }
        return res;
    }

    @Override
    public List<Tag> getExpenseTagList(int count) {
        Realm realm = RealmDelegate.getInstance();
        List<Tag> res = realm.where(Tag.class)
                .equalTo(Tag.Structure.CAT + "." + Category.Structure.TYPE, Category.TYPE_EXPENSE)
                .findAllSorted(Tag.Structure.ID, Sort.ASCENDING);
        if (count > 0) {
            int size = res.size();
            if (size > count) {
                res = realm.where(Tag.class).greaterThanOrEqualTo(Tag.Structure.ID, res.get(size - count).getId())
                        .findAllSorted(Tag.Structure.ID, Sort.ASCENDING);
            }
        }
        return res;
    }

    @Override
    public void updateTag(Tag tag) {
        Realm realm = RealmDelegate.getInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(tag);
        realm.commitTransaction();
    }

    @Override
    public Tag increaseTag(long id) {
        Realm realm = RealmDelegate.getInstance();
        realm.beginTransaction();
        Tag tag = realm.where(Tag.class).equalTo(Tag.Structure.ID, id).findFirst();
        tag.increaseCount();
        realm.commitTransaction();
        return tag;
    }
}
