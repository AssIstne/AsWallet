package com.assistne.aswallet.database.bean;

import android.text.format.DateFormat;

import com.orhanobut.logger.Logger;

import java.util.Calendar;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.Required;

/**
 * 决定数据库的结构, 提供给{@link com.assistne.aswallet.database.dao.BillDao}使用
 * Created by assistne on 15/11/16.
 */
public class Bill extends RealmObject {
    public static final int TYPE_INCOME = 1;
    public static final int TYPE_EXPENSE = 0;

    public interface Structure {
        String ID = "id";
        String DES = "description";
        String CAT = "category";
        String DATE = "date";
        String TYPE = "type";
        String PRICE = "price";
    }

    @Index
    private long id;
    private String description;
    private Category category;
    @Required
    private Date date;
    private int type;
    private float price;

    public Bill() {
        date = new Date(Calendar.getInstance().getTimeInMillis());
        type = TYPE_EXPENSE;
        description = "";
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return super.toString() + "\n\tid   : " + id + "" +
                "\n\tprice: " + price +
                "\n\tcat  : " + (category == null ? "no-cat" : category.getName()) +
                "\n\tdate : " + DateFormat.format("yyyy-MM-dd HH:mm:ss", date) +
                "\n\ttype : " + (type == TYPE_INCOME ? "income" : "expense") +
                "\n\tdes  : " + description ;
    }
}
