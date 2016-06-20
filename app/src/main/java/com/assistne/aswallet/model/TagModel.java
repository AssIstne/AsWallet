package com.assistne.aswallet.model;

import android.os.Parcel;

/**
 * Created by assistne on 16/6/6.
 */
public class TagModel extends Model {
    private String name;
    private long categoryId;
    private boolean isActive;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return super.toString() +
                "\n\tid   :" + id +
                "\n\tname :" + name +
                "\n\tcatId :" + categoryId;
    }


    public TagModel() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeLong(this.categoryId);
        dest.writeByte(this.isActive ? (byte) 1 : (byte) 0);
        dest.writeLong(this.id);
    }

    protected TagModel(Parcel in) {
        this.name = in.readString();
        this.categoryId = in.readLong();
        this.isActive = in.readByte() != 0;
        this.id = in.readLong();
    }

    public static final Creator<TagModel> CREATOR = new Creator<TagModel>() {
        @Override
        public TagModel createFromParcel(Parcel source) {
            return new TagModel(source);
        }

        @Override
        public TagModel[] newArray(int size) {
            return new TagModel[size];
        }
    };
}
