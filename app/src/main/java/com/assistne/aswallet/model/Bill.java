package com.assistne.aswallet.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateFormat;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.util.Calendar;

/**
 * Created by assistne on 15/11/16.
 */
public class Bill extends DataSupport implements Parcelable{
    public static final int TYPE_INCOME = 0;
    public static final int TYPE_EXPENSE = 1;

    private int id;
    private String description;

    private Category category;

    @Column(nullable = false)
    private long dateInMills;

    private int type;
    private float price;

    public Bill() {
        dateInMills = Calendar.getInstance().getTimeInMillis();
        type = TYPE_EXPENSE;
        description = "";
    }

    public long getDateInMills() {
        return dateInMills;
    }

    public void setDateInMills(long dateInMills) {
        this.dateInMills = dateInMills;
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

    public int getId() {
        return id;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeParcelable(category, flags);
        dest.writeLong(dateInMills);
        dest.writeInt(type);
        dest.writeInt(id);
        dest.writeFloat(price);
    }

    public static final Parcelable.Creator<Bill> CREATOR
            = new Parcelable.Creator<Bill>() {
        public Bill createFromParcel(Parcel in) {
            Bill bill = new Bill();
            bill.description = in.readString();
            bill.category = in.readParcelable(Category.class.getClassLoader());
            bill.dateInMills = in.readLong();
            bill.type = in.readInt();
            bill.id = in.readInt();
            bill.price = in.readFloat();
            return bill;
        }

        public Bill[] newArray(int size) {
            return new Bill[size];
        }
    };

    @Override
    public String toString() {
        return super.toString() + "\n\tid   : " + id + "" +
                "\n\tprice: " + price +
                "\n\tcat  : " + category.getName() +
                "\n\tdate : " + DateFormat.format("yyyy-MM-dd HH:mm:ss", dateInMills) +
                "\n\ttype : " + (type == TYPE_INCOME ? "income" : "expense") +
                "\n\tdes  : " + description ;
    }
}
