package com.assistne.aswallet.model;

import android.os.Parcelable;

/**
 * 方便在Adapter中统一表示3种Model
 * Created by assistne on 16/6/20.
 */
public abstract class Model implements Parcelable{
    protected long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
