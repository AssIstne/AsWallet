package com.assistne.aswallet.component;

import android.app.Application;

import com.assistne.aswallet.database.PrimaryKeyFactory;
import com.assistne.aswallet.database.bean.Category;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by assistne on 15/12/24.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init().logLevel(LogLevel.FULL).methodCount(1).hideThreadInfo();
        /** Realm暂时不支持自增主键, 需要自己弄id */
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder(this).name("AsWallet.realm").build());
        Realm realm = Realm.getDefaultInstance();
        PrimaryKeyFactory.getInstance().initialize(realm);

        // TODO: 16/5/19 测试用
        if (realm.where(Category.class).count() == 0) {
            realm.beginTransaction();
            Category category = realm.createObject(Category.class);
            category.setId(PrimaryKeyFactory.nextCategoryKey());
            category.setName("餐饮");
            category.setIconType(0);
            category.setActivate(true);
            realm.commitTransaction();
            realm.beginTransaction();
            category = realm.createObject(Category.class);
            category.setId(PrimaryKeyFactory.nextCategoryKey());
            category.setName("娱乐");
            category.setIconType(1);
            category.setActivate(true);
            realm.commitTransaction();
            realm.beginTransaction();
            category = realm.createObject(Category.class);
            category.setId(PrimaryKeyFactory.nextCategoryKey());
            category.setName("医疗");
            category.setIconType(2);
            category.setActivate(true);
            realm.commitTransaction();
            realm.beginTransaction();
            category = realm.createObject(Category.class);
            category.setId(PrimaryKeyFactory.nextCategoryKey());
            category.setName("教育");
            category.setIconType(3);
            category.setActivate(true);
            realm.commitTransaction();
            realm.beginTransaction();
            category = realm.createObject(Category.class);
            category.setId(PrimaryKeyFactory.nextCategoryKey());
            category.setName("出行");
            category.setIconType(4);
            category.setActivate(true);
            realm.commitTransaction();
            realm.beginTransaction();
            category = realm.createObject(Category.class);
            category.setId(PrimaryKeyFactory.nextCategoryKey());
            category.setName("交通");
            category.setIconType(5);
            category.setActivate(true);
            realm.commitTransaction();
            realm.beginTransaction();
            category = realm.createObject(Category.class);
            category.setId(PrimaryKeyFactory.nextCategoryKey());
            category.setName("社交");
            category.setIconType(6);
            category.setActivate(true);
            realm.commitTransaction();
        }
        realm.close();
    }
}
