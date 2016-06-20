package com.assistne.aswallet.category;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
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
import com.assistne.aswallet.model.Model;
import com.assistne.aswallet.model.TagModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by assistne on 16/3/22.
 */
public class EditCategoryAdapter extends RecyclerView.Adapter implements
        View.OnClickListener, View.OnLongClickListener, CompoundButton.OnCheckedChangeListener {
    private static final int TYPE_CAT = 1;
    private static final int TYPE_TAG = 2;
    private List<Model> mData;
    private OnItemClickListener mListener;
    private boolean mIsEditing;
    private SparseIntArray mCatSelectArray;
    private SparseIntArray mTagSelectArray;

    public EditCategoryAdapter() {
        this(new ArrayList<Model>());
    }

    public EditCategoryAdapter(List<Model> data) {
        mData = data == null ? new ArrayList<Model>() : data;
        mCatSelectArray = new SparseIntArray();
        mTagSelectArray = new SparseIntArray();
    }

    /** 列表包含Category和Tag两类 */   @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_CAT) {
            return new CategoryHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_edit_cat, parent, false));
        } else {
            return new TagHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_edit_tag, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Model model = mData.get(position);
        // 根据model的类型来分
        if (model instanceof CategoryModel) {
            CategoryModel category = (CategoryModel) model;
            CategoryHolder holder = (CategoryHolder)viewHolder;
            holder.catId = category.getId();
            holder.name.setText(category.getName());
            holder.icon.setImageResource(category.getIconRes());
            holder.itemView.setTag(category);
            // 在判断是否选中的时候用到
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
        } else {
            TagModel tag = (TagModel) model;
            final TagHolder holder = (TagHolder) viewHolder;
            holder.tagId = tag.getId();
            holder.name.setText(tag.getName());
            holder.switched.setChecked(tag.isActive());
            // 在判断是否选中的时候用到
            holder.switched.setTag(position);
            if (mIsEditing) {
                holder.selectCbx.setAlpha(0);
                holder.selectCbx.setVisibility(View.VISIBLE);
                AnimatorSet set = new AnimatorSet();
                set.playTogether(
                        ObjectAnimator.ofFloat(holder.selectCbx, "alpha", 1),
                        ObjectAnimator.ofFloat(holder.switched, "alpha", 0)
                );
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        holder.switched.setVisibility(View.INVISIBLE);
                    }
                });
                set.start();
            } else {
                holder.selectCbx.setChecked(false);
                holder.switched.setAlpha(0);
                holder.switched.setVisibility(View.VISIBLE);
                AnimatorSet set = new AnimatorSet();
                set.playTogether(
                        ObjectAnimator.ofFloat(holder.selectCbx, "alpha", 0),
                        ObjectAnimator.ofFloat(holder.switched, "alpha", 1)
                );
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        holder.selectCbx.setVisibility(View.INVISIBLE);
                    }
                });
                set.start();
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position) instanceof CategoryModel ? TYPE_CAT : TYPE_TAG;
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

    public void insert(int position, Model model) {
        position = position < 0 || position >= mData.size() ? mData.size() : position;
        mData.add(position, model);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, mData.size());
    }

    /**
     * 添加多个item到position的后面, 所以position要+1 */
    public void insertList(int position, List<? extends Model> modelList) {
        if (modelList != null && modelList.size() != 0) {
            position += 1;
            position = position < 0 || position >= mData.size() ? mData.size() : position;
            mData.addAll(position, modelList);
            notifyItemRangeInserted(position, modelList.size());
            notifyItemRangeChanged(position, mData.size());
        }
    }

    // TODO: 16/6/20 3种情况, 编辑状态下改变CAT的cbx, 改变TAG的cbx, 普通状态改变TAG的switch
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int position = (int) buttonView.getTag();
        if (position >= 0 && position < mData.size()) {
            switch (buttonView.getId()) {
                case R.id.list_item_edit_cat_cbx_select:
                    if (isChecked) {
                        mCatSelectArray.append(position, position);
                    } else {
                        mCatSelectArray.delete(position);
                    }
                    break;
                case R.id.list_item_edit_tag_switch:
                    if (!isEditing() && mListener != null) {
                        mListener.onTagSwitchChange(buttonView, isChecked);
                    }
                    break;
                case R.id.list_item_edit_tag_cbx_select:
                    if (isChecked) {
                        mTagSelectArray.append(position, position);
                    } else {
                        mTagSelectArray.delete(position);
                    }
                    break;
            }
        }
    }

    public class CategoryHolder extends RecyclerView.ViewHolder {
        public long catId;
        @Bind(R.id.list_item_edit_cat_icon) public ImageView icon;
        @Bind(R.id.list_item_edit_cat_name) public TextView name;
        @Bind(R.id.list_item_edit_cat_cbx_select) public CheckBox selectCbx;

        public CategoryHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(EditCategoryAdapter.this);
            itemView.setOnLongClickListener(EditCategoryAdapter.this);
            selectCbx.setOnCheckedChangeListener(EditCategoryAdapter.this);
        }
    }

    public class TagHolder extends RecyclerView.ViewHolder {
        public long tagId;
        @Bind(R.id.list_item_edit_tag_name) public TextView name;
        @Bind(R.id.list_item_edit_tag_switch) public SwitchCompat switched;
        @Bind(R.id.list_item_edit_tag_cbx_select) public CheckBox selectCbx;
        public TagHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(EditCategoryAdapter.this);
            selectCbx.setOnCheckedChangeListener(EditCategoryAdapter.this);
            switched.setOnCheckedChangeListener(EditCategoryAdapter.this);
        }
    }

    public void setOnItemClickListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    public interface OnItemClickListener {
        void onClick(View v);
        boolean onLongClick(View v);
        void onTagSwitchChange(CompoundButton view, boolean isChecked);
    }

    public boolean isEditing() {
        return mIsEditing;
    }

    public void enterEditMode() {
        mIsEditing = true;
        // 清空选择
        mCatSelectArray.clear();
        notifyDataSetChanged();
    }

    public void exitEditMode() {
        mIsEditing = false;
        // 清空选择
        mCatSelectArray.clear();
        notifyDataSetChanged();
    }

    public boolean hasSelected() {
        return mCatSelectArray.size() > 0 || mTagSelectArray.size() > 0;
    }

    /**
     * key是在{@link #mData}的position, value是对应的{@link CategoryModel#getId()} */
    @NonNull
    public SparseLongArray getSelectedList() {
        SparseLongArray list = new SparseLongArray();
        if (mCatSelectArray.size() > 0) {
            for (int i = 0; i < mCatSelectArray.size(); i++) {
                int position = mCatSelectArray.valueAt(i);
                if (position >= 0 && position < mData.size()) {
                    list.append(position, mData.get(position).getId());
                }
            }
        }
        return list;
    }
    // TODO: 16/6/20 删除TAG怎么删?
    // TODO: 16/6/20 新增TAG怎么新增?
}
