package com.assistne.aswallet.model;

import android.os.Parcel;
import android.text.format.DateFormat;

import com.assistne.aswallet.R;
import com.assistne.aswallet.database.bean.Bill;

import java.util.Date;


/**
 * 提供给Mvp.View展示内容, 赋值依据是{@link Bill}
 * Created by assistne on 16/5/21.
 */
public class BillModel extends Model{

    private String description;
    private String categoryName;
    private long categoryId;
    private int categoryIconRes;
    private long tagId;
    private String tagName;
    private Date date;
    private int type;
    private float price;

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

    public boolean isIncome() {
        return type == Bill.TYPE_INCOME;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public long getTagId() {
        return tagId;
    }

    public void setTagId(long tagId) {
        this.tagId = tagId;
    }

    public void clearTag() {
        tagId = -1;
        tagName = "";
    }

    @Override
    public String toString() {
        return super.toString() + "\n\tid   : " + id + "" +
                "\n\tprice: " + price +
                "\n\tcat  : " + categoryName +
                "\n\tcatId: " + categoryId +
                "\n\ttag  : " + tagName +
                "\n\ttagId: " + tagId +
                "\n\tdate : " + (date == null ? "no date" : DateFormat.format("yyyy-MM-dd HH:mm:ss", date)) +
                "\n\ttype : " + (type == Bill.TYPE_INCOME ? "income" : "expense") +
                "\n\tdes  : " + description ;
    }


    public BillModel() {
        id = -1;
        description = "";
        type = Bill.TYPE_EXPENSE;
    }

    public int getCategoryIconRes() {
        return categoryIconRes;
    }

    public void setCategoryIconRes(int categoryIconRes) {
        this.categoryIconRes = categoryIconRes;
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
        dest.writeLong(this.categoryId);
        dest.writeInt(this.categoryIconRes);
        dest.writeLong(this.tagId);
        dest.writeString(this.tagName);
        dest.writeLong(date != null ? date.getTime() : -1);
        dest.writeInt(this.type);
        dest.writeFloat(this.price);
    }

    protected BillModel(Parcel in) {
        this.id = in.readLong();
        this.description = in.readString();
        this.categoryName = in.readString();
        this.categoryId = in.readLong();
        this.categoryIconRes = in.readInt();
        this.tagId = in.readLong();
        this.tagName = in.readString();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.type = in.readInt();
        this.price = in.readFloat();
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
}
