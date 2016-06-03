package com.assistne.aswallet.billdetail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.assistne.aswallet.R;
import com.assistne.aswallet.model.CategoryModel;
import com.assistne.aswallet.model.ModelTool;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by assistne on 16/3/22.
 */
public class CategoryAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private List<CategoryModel> mData;
    private OnItemClickListener mListener;

    public CategoryAdapter() {
        this(new ArrayList<CategoryModel>());
    }

    public CategoryAdapter(List<CategoryModel> data) {
        mData = data == null ? new ArrayList<CategoryModel>() : data;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_category, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        CategoryModel category = mData.get(position);
        Logger.d(category.toString());
        Holder holder = (Holder)viewHolder;
        holder.root.setTag(category);
        holder.name.setText(category.getName());
        holder.icon.setImageResource(category.getIconRes());
    }

    public void setData(List<CategoryModel> data) {
        if (data != null) {
            mData.clear();
            mData.addAll(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onClick(View v) {
        Object tag = v.getTag();
        if (mListener != null && tag != null && tag instanceof CategoryModel) {
            mListener.onClick((CategoryModel)tag);
        }
    }

    private class Holder extends RecyclerView.ViewHolder {
        public View root;
        public ImageView icon;
        public TextView name;

        public Holder(View itemView) {
            super(itemView);
            root = itemView;
            icon = (ImageView) itemView.findViewById(R.id.list_item_category_icon);
            name = (TextView) itemView.findViewById(R.id.list_item_category_name);
            itemView.setOnClickListener(CategoryAdapter.this);
        }
    }

    public void setOnItemClickListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    public interface OnItemClickListener {
        void onClick(CategoryModel category);
    }
}
