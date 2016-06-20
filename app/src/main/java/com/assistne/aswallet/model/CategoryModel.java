package com.assistne.aswallet.model;

import android.os.Parcel;

import com.assistne.aswallet.database.bean.Category;

/**
 * 提供给Mvp.View展示内容, 赋值依据是{@link Category}
 * Created by assistne on 16/5/21.
 */
public class CategoryModel extends Model {
    private String name;
    private int type;
    private boolean isActivate;
    private int iconType;

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
        return ModelTool.convertIconType(iconType);
    }

    public int getIconType() {
        return iconType;
    }

    public void setIconType(int iconType) {
        this.iconType = iconType;
    }

    /** 是否可修改, 默认的类别不能删减 */
    public boolean isEditable() {
        return id > 9;
    }

    @Override
    public String toString() {
        return super.toString() +
                "\n\tid   :" + id +
                "\n\tname :" + name +
                "\n\tact  :" + isActivate +
                "\n\teditable  :" + isEditable() +
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
        dest.writeInt(this.iconType);
    }

    public CategoryModel() {
    }

    protected CategoryModel(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.type = in.readInt();
        this.isActivate = in.readByte() != 0;
        this.iconType = in.readInt();
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
