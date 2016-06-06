package com.assistne.aswallet.database.bean;

import io.realm.RealmObject;

/**
 * 1. 当没有描述的时候代替描述
 * 2. 类别的二级分类
 * 3. 可以多选, 同一个类别的tag只能选一个
 * Created by assistne on 16/6/6.
 */
public class Tag extends RealmObject {

    public interface Structure {
        String ID = "id";
        String NAME = "name";
        String CAT = "category";
        String ACTIVATE = "isActivate";
        String COUNT = "count";
    }

    private long id;
    private String name;
    private Category category;
    private boolean isActivate;
    private long count;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isActivate() {
        return isActivate;
    }

    public void setActivate(boolean activate) {
        isActivate = activate;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
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
                "\n\tcatId :" + category.getId() +
                "\n\tcount :" + count;
    }
}
