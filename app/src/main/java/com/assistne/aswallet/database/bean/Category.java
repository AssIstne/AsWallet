package com.assistne.aswallet.database.bean;

import com.assistne.aswallet.database.PrimaryKeyFactory;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * 决定数据库的结构, 提供给{@link com.assistne.aswallet.database.dao.CategoryDao}使用
 * Created by assistne on 15/11/16.
 */
public class Category extends RealmObject {
    public static final int TYPE_INCOME = 1;
    public static final int TYPE_EXPENSE = 0;

    public interface Structure {
        String ID = "id";
        String NAME = "name";
        String TYPE = "type";
        String ACTIVATE = "isActivate";
        String ICON = "iconType";
        String COUNT = "count";
    }

    public interface Type {
        int INCOME = 1;
        int FOOD = 2;
        int ENTERTAINMENT = 3;
        int HOSPITAL = 4;
        int EDUCATION = 5;
        int TRAVEL = 6;
        int TRAFFIC = 7;
        int SOCIAL = 8;
        int SHOPPING = 9;
    }

    @PrimaryKey
    private long id;
    private String name;
    private int type;
    private boolean isActivate;
    private int iconType;
    private long count;

    public Category(){
        name = "undefine";
        type = TYPE_EXPENSE;
        isActivate = true;
        iconType = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isActivate() {
        return isActivate;
    }

    public void setActivate(boolean activate) {
        isActivate = activate;
    }

    public int getIconType() {
        return iconType;
    }

    public void setIconType(int iconType) {
        this.iconType = iconType;
    }

    public long getCount() {
        return count;
    }

    public void increaseCount() {
        this.count += 1;
    }

    @Override
    public String toString() {
        return super.toString() +
                "\n\tid   :" + id +
                "\n\tname :" + name +
                "\n\tact  :" + isActivate +
                "\n\ticon :" + iconType +
                "\n\tcount :" + count +
                "\n\ttype :" + (type == TYPE_INCOME ? "income" : "expense");
    }
}
