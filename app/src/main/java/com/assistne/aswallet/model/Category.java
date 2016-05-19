package com.assistne.aswallet.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by assistne on 15/11/16.
 */
public class Category extends DataSupport implements Parcelable {
    public static final int TYPE_INCOME = 0;
    public static final int TYPE_EXPENSE = 1;

    private int id;
    @Column(nullable = false, defaultValue = "unknown")
    private String name;
    @Column(nullable = false)
    private int type;
    @Column(defaultValue = "1")
    private int isActivate;

    private List<Bill> bills = new ArrayList<>();

    private Category(){
        this("default");
    }

    public Category(@NonNull String name) {
        this.name = name;
        type = TYPE_EXPENSE;
        isActivate = 1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsActivate() {
        return isActivate;
    }

    public void setIsActivate(int isActivate) {
        this.isActivate = isActivate;
    }

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(ArrayList<Bill> bills) {
        this.bills = bills;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeList(bills);
        dest.writeInt(isActivate);
        dest.writeInt(type);
        dest.writeInt(id);
    }

    public static final Parcelable.Creator<Category> CREATOR
            = new Parcelable.Creator<Category>() {
        public Category createFromParcel(Parcel in) {
            Category category = new Category();
            category.name = in.readString();
            category.bills = new ArrayList<>();
            in.readList(category.bills, null);
            category.isActivate = in.readInt();
            category.type = in.readInt();
            category.id = in.readInt();
            return category;
        }

        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    @Override
    public String toString() {
        return super.toString() +
                "\n\tid   :" + id +
                "\n\tname :" + name +
                "\n\tbillN:" + bills.size() +
                "\n\tact  :" + isActivate +
                "\n\ttype :" + (type == TYPE_INCOME ? "income" : "expense");
    }
}
