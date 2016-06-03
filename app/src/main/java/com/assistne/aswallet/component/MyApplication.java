package com.assistne.aswallet.component;

import android.app.Application;
import android.content.Context;

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

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        if (mContext == null) {
            mContext = this;
        }
        Logger.init().logLevel(LogLevel.FULL).methodCount(4).hideThreadInfo();
        /** Realm暂时不支持自增主键, 需要自己弄id */
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder(this).name("AsWallet.realm").build());
        Realm realm = Realm.getDefaultInstance();
        PrimaryKeyFactory.getInstance().initialize(realm);

        /** 创建默认类别 */
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
            category.setName("旅行");
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
            realm.beginTransaction();
            category = realm.createObject(Category.class);
            category.setId(PrimaryKeyFactory.nextCategoryKey());
            category.setName("购物");
            category.setIconType(7);
            category.setActivate(true);
            realm.commitTransaction();
            realm.beginTransaction();
            category = realm.createObject(Category.class);
            category.setId(PrimaryKeyFactory.nextCategoryKey());
            category.setName("收入");
            category.setType(Category.TYPE_INCOME);
            category.setIconType(8);
            category.setActivate(true);
            realm.commitTransaction();
        }
        realm.close();
    }

    public static Context getStaticContext() {
        return mContext;
    }
}
