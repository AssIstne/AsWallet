package com.assistne.aswallet.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.text.format.DateFormat;

import com.assistne.aswallet.R;
import com.assistne.aswallet.database.bean.Bill;

import java.util.Date;


/**
 * 提供给Mvp.View展示内容, 赋值依据是{@link Bill}
 * Created by assistne on 16/5/21.
 */
public class BillModel implements Parcelable{

    private long id;
    private String description;
    private String categoryName;
    private long categoryId;
    private int categoryIconRes;
    private Date date;
    private int type;
    private float price;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public int getTypeStringRes() {
        return (type == Bill.TYPE_INCOME ? R.string.income : R.string.expense);
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return super.toString() + "\n\tid   : " + id + "" +
                "\n\tprice: " + price +
                "\n\tcat  : " + categoryName +
                "\n\tcatId: " + categoryId +
                "\n\tdate : " + DateFormat.format("yyyy-MM-dd HH:mm:ss", date) +
                "\n\ttype : " + (type == Bill.TYPE_INCOME ? "income" : "expense") +
                "\n\tdes  : " + description ;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.description);
        dest.writeString(this.categoryName);
        dest.writeSerializable(this.date);
        dest.writeInt(this.type);
        dest.writeFloat(this.price);
        dest.writeLong(this.categoryId);
        dest.writeInt(this.categoryIconRes);
    }

    public BillModel() {
        id = -1;
        description = "";
        type = Bill.TYPE_EXPENSE;
        // TODO: 16/5/22 应该是点击保存的时候的时间
        date = new Date();
    }

    protected BillModel(Parcel in) {
        this.id = in.readLong();
        this.description = in.readString();
        this.categoryName = in.readString();
        this.date = (Date) in.readSerializable();
        this.type = in.readInt();
        this.price = in.readFloat();
        this.categoryId = in.readLong();
        this.categoryIconRes = in.readInt();
    }

    public static final Creator<BillModel> CREATOR = new Creator<BillModel>() {
        @Override
        public BillModel createFromParcel(Parcel source) {
            return new BillModel(source);
        }

        @Override
        public BillModel[] newArray(int size) {
            return new BillModel[size];
        }
    };

    public int getCategoryIconRes() {
        return categoryIconRes;
    }

    public void setCategoryIconRes(int categoryIconRes) {
        this.categoryIconRes = categoryIconRes;
    }
}
