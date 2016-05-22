package com.assistne.aswallet.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.assistne.aswallet.database.bean.Category;

/**
 * 提供给Mvp.View展示内容, 赋值依据是{@link Category}
 * Created by assistne on 16/5/21.
 */
public class CategoryModel implements Parcelable {
    private long id;
    private String name;
    private int type;
    private boolean isActivate;
    private int iconRes;

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

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }

    @Override
    public String toString() {
        return super.toString() +
                "\n\tid   :" + id +
                "\n\tname :" + name +
                "\n\tact  :" + isActivate +
                "\n\ttype :" + (type == Category.TYPE_INCOME ? "income" : "expense");
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.type);
        dest.writeByte(this.isActivate ? (byte) 1 : (byte) 0);
        dest.writeInt(this.iconRes);
    }

    public CategoryModel() {
    }

    protected CategoryModel(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.type = in.readInt();
        this.isActivate = in.readByte() != 0;
        this.iconRes = in.readInt();
    }

    public static final Creator<CategoryModel> CREATOR = new Creator<CategoryModel>() {
        @Override
        public CategoryModel createFromParcel(Parcel source) {
            return new CategoryModel(source);
        }

        @Override
        public CategoryModel[] newArray(int size) {
            return new CategoryModel[size];
        }
    };
}
