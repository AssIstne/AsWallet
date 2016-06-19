package com.assistne.aswallet.component;

import android.app.Application;
import android.content.Context;

import com.assistne.aswallet.database.PrimaryKeyFactory;
import com.assistne.aswallet.database.bean.Category;
import com.assistne.aswallet.database.bean.Tag;
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
        Category category;
        Category foodCat = null;
        Category trafficCat = null;
        /** 创建默认类别 */
        if (realm.where(Category.class).count() == 0) {
            realm.beginTransaction();
            category = realm.createObject(Category.class);
            category.setId(Category.Type.INCOME);
            category.setName("收入");
            category.setType(Category.TYPE_INCOME);
            category.setIconType(Category.Type.INCOME);
            category.setActivate(true);
            realm.commitTransaction();
            realm.beginTransaction();
            foodCat = realm.createObject(Category.class);
            foodCat.setId(Category.Type.FOOD);
            foodCat.setName("餐饮");
            foodCat.setIconType(Category.Type.FOOD);
            foodCat.setActivate(true);
            realm.commitTransaction();
            realm.beginTransaction();
            category = realm.createObject(Category.class);
            category.setId(Category.Type.ENTERTAINMENT);
            category.setName("娱乐");
            category.setIconType(Category.Type.ENTERTAINMENT);
            category.setActivate(true);
            realm.commitTransaction();
            realm.beginTransaction();
            category = realm.createObject(Category.class);
            category.setId(Category.Type.HOSPITAL);
            category.setName("医疗");
            category.setIconType(Category.Type.HOSPITAL);
            category.setActivate(true);
            realm.commitTransaction();
            realm.beginTransaction();
            category = realm.createObject(Category.class);
            category.setId(Category.Type.EDUCATION);
            category.setName("教育");
            category.setIconType(Category.Type.EDUCATION);
            category.setActivate(true);
            realm.commitTransaction();
            realm.beginTransaction();
            category = realm.createObject(Category.class);
            category.setId(Category.Type.TRAVEL);
            category.setName("旅行");
            category.setIconType(Category.Type.TRAVEL);
            category.setActivate(true);
            realm.commitTransaction();
            realm.beginTransaction();
            trafficCat = realm.createObject(Category.class);
            trafficCat.setId(Category.Type.TRAFFIC);
            trafficCat.setName("交通");
            trafficCat.setIconType(Category.Type.TRAFFIC);
            trafficCat.setActivate(true);
            realm.commitTransaction();
            realm.beginTransaction();
            category = realm.createObject(Category.class);
            category.setId(Category.Type.SOCIAL);
            category.setName("社交");
            category.setIconType(Category.Type.SOCIAL);
            category.setActivate(true);
            realm.commitTransaction();
            realm.beginTransaction();
            category = realm.createObject(Category.class);
            category.setId(Category.Type.SHOPPING);
            category.setName("购物");
            category.setIconType(Category.Type.SHOPPING);
            category.setActivate(true);
            realm.commitTransaction();
        }
        /** Category是手动给ID赋值, 所以在创建完Category之后才初始化主键生成器 */
        PrimaryKeyFactory.getInstance().initialize(realm);
        Tag tag;
        if (realm.where(Tag.class).count() == 0) {
            realm.beginTransaction();
            tag = realm.createObject(Tag.class);
            tag.setId(PrimaryKeyFactory.nextTagKey());
            tag.setName("早餐");
            tag.setCategory(foodCat);
            tag.setActivate(true);
            realm.commitTransaction();
            realm.beginTransaction();
            tag = realm.createObject(Tag.class);
            tag.setId(PrimaryKeyFactory.nextTagKey());
            tag.setName("午餐");
            tag.setCategory(foodCat);
            tag.setActivate(true);
            realm.commitTransaction();
            realm.beginTransaction();
            tag = realm.createObject(Tag.class);
            tag.setId(PrimaryKeyFactory.nextTagKey());
            tag.setName("晚餐");
            tag.setCategory(foodCat);
            tag.setActivate(true);
            realm.commitTransaction();
            realm.beginTransaction();
            tag = realm.createObject(Tag.class);
            tag.setId(PrimaryKeyFactory.nextTagKey());
            tag.setName("羊城通");
            tag.setCategory(trafficCat);
            tag.setActivate(true);
            realm.commitTransaction();
            realm.beginTransaction();
            tag = realm.createObject(Tag.class);
            tag.setId(PrimaryKeyFactory.nextTagKey());
            tag.setName("小白");
            tag.setCategory(trafficCat);
            tag.setActivate(true);
            realm.commitTransaction();
        }
        realm.close();
    }

    public static Context getStaticContext() {
        return mContext;
    }
}
