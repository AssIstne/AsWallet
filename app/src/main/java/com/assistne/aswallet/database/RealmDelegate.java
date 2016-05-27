package com.assistne.aswallet.database;

import io.realm.Realm;

/**
 * 因为Realm逗逼, 统一在这里做Realm实例的获得的关闭
 * Created by assistne on 16/5/24.
 */
public class RealmDelegate {
    private static Realm REALM_INSTANCE;

    /**
     * 在{@link com.assistne.aswallet.component.BaseActivity} 中调用*/
    public static void start() {
        REALM_INSTANCE = Realm.getDefaultInstance();
    }

    /**
     * 在{@link com.assistne.aswallet.component.BaseActivity} 中调用, 一定要先调用{@link #start()}*/
    public static void close() {
        if (REALM_INSTANCE != null) {
            REALM_INSTANCE.close();
        }
    }

    public static Realm getInstance() {
        if (REALM_INSTANCE == null) {
            throw new IllegalStateException("Has you called RealmDelegate.start()?");
        }
        return REALM_INSTANCE;
    }
}
