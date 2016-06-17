package com.assistne.aswallet.category;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.util.SparseLongArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.assistne.aswallet.R;
import com.assistne.aswallet.model.CategoryModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by assistne on 16/3/22.
 */
public class CategoryAdapter extends RecyclerView.Adapter implements
        View.OnClickListener, View.OnLongClickListener, CompoundButton.OnCheckedChangeListener {
    private List<CategoryModel> mData;
    private OnItemClickListener mListener;
    private boolean mIsEditing;
    private SparseIntArray mSelectArray;

    public CategoryAdapter() {
        this(new ArrayList<CategoryModel>());
    }

    public CategoryAdapter(List<CategoryModel> data) {
        mData = data == null ? new ArrayList<CategoryModel>() : data;
        mSelectArray = new SparseIntArray();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_edit_cat, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        CategoryModel category = mData.get(position);
        Holder holder = (Holder)viewHolder;
        holder.itemView.setTag(category);
        holder.name.setText(category.getName());
        holder.icon.setImageResource(category.getIconRes());
        holder.selectCbx.setTag(position);
        if (mIsEditing) {
            holder.selectCbx.setAlpha(0);
            holder.selectCbx.setVisibility(View.VISIBLE);
            holder.selectCbx.animate().alpha(1).start();
        } else {
            holder.selectCbx.setChecked(false);
            holder.selectCbx.setAlpha(1);
            holder.selectCbx.animate().alpha(0).start();
            holder.selectCbx.setVisibility(View.INVISIBLE);
        }
        holder.selectCbx.setEnabled(category.isEditable());
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
        if (mListener != null) {
            mListener.onClick(v);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return mListener != null && mListener.onLongClick(v);
    }

    public void remove(List<Integer> positionList) {
        /** 移除前需要先从大到小排序, 因为在{@link List}中序号较小的删除后, 较大的需要减一才能保证不报错, 从大到小删除则没有该问题
         * */
        Collections.sort(positionList);
        Collections.reverse(positionList);
        for (int position : positionList) {
            mData.remove(position);
            notifyItemRemoved(position);
        }
        notifyItemRangeChanged(positionList.get(0), mData.size());
    }

    public void insert(int position, CategoryModel model) {
        position = position < 0 || position >= mData.size() ? mData.size() : position;
        mData.add(position, model);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, mData.size());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int position = (int) buttonView.getTag();
        if (position > 0 && position < mData.size()) {
            if (isChecked) {
                mSelectArray.append(position, position);
            } else {
                mSelectArray.delete(position);
            }
        }
    }

    public class Holder extends RecyclerView.ViewHolder {
        @Bind(R.id.list_item_edit_cat_icon) public ImageView icon;
        @Bind(R.id.list_item_edit_cat_name) public TextView name;
        @Bind(R.id.list_item_edit_cat_cbx_select) public CheckBox selectCbx;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(CategoryAdapter.this);
            itemView.setOnLongClickListener(CategoryAdapter.this);
            selectCbx.setOnCheckedChangeListener(CategoryAdapter.this);
        }
    }

    public void setOnItemClickListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    public interface OnItemClickListener {
        void onClick(View v);
        boolean onLongClick(View v);
    }

    public boolean isEditing() {
        return mIsEditing;
    }

    public void enterEditMode() {
        mIsEditing = true;
        // 清空选择
        mSelectArray.clear();
        notifyDataSetChanged();
    }

    public void exitEditMode() {
        mIsEditing = false;
        // 清空选择
        mSelectArray.clear();
        notifyDataSetChanged();
    }

    public boolean hasSelected() {
        return mSelectArray.size() > 0;
    }

    /**
     * key是在{@link #mData}的position, value是对应的{@link CategoryModel#getId()} */
    @NonNull
    public SparseLongArray getSelectedList() {
        SparseLongArray list = new SparseLongArray();
        if (mSelectArray.size() > 0) {
            for (int i = 0; i < mSelectArray.size(); i++) {
                int position = mSelectArray.valueAt(i);
                if (position > 0 && position < mData.size()) {
                    list.append(position, mData.get(position).getId());
                }
            }
        }
        return list;
    }
}
