package com.assistne.aswallet.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by assistne on 16/6/6.
 */
public class TagModel implements Parcelable {
    private long id;
    private String name;
    private long categoryId;

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

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }


    @Override
    public String toString() {
        return super.toString() +
                "\n\tid   :" + id +
                "\n\tname :" + name +
                "\n\tcatId :" + categoryId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeLong(this.categoryId);
    }

    public TagModel() {
    }

    protected TagModel(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.categoryId = in.readLong();
    }

    public static final Parcelable.Creator<TagModel> CREATOR = new Parcelable.Creator<TagModel>() {
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
