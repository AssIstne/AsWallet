package com.assistne.aswallet.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.assistne.aswallet.R;
import com.assistne.aswallet.component.MyApplication;
import com.assistne.aswallet.model.BillModel;
import com.assistne.aswallet.tools.FormatUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by assistne on 16/3/11.
 */
public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder> implements View.OnClickListener{

    private List<BillModel> mData;
    private ItemClickListener mListener;


    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.list_item_img_icon) public ImageView imgIcon;
        @Bind(R.id.list_item_text_date) public TextView tvDate;
        @Bind(R.id.list_item_text_description) public TextView tvDescription;
        @Bind(R.id.list_item_text_price) public TextView tvPrice;
        @Bind(R.id.smMenuViewRight) public ImageButton btnDelete;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            v.setOnClickListener(HomeListAdapter.this);
            btnDelete.setOnClickListener(HomeListAdapter.this);
        }
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            if (v.getId() == R.id.smMenuViewRight) {
                mListener.onMenuClick(v);
            } else {
                mListener.onItemClick((int) v.getTag());
            }
        }
    }

    public HomeListAdapter() {
        this(null);
    }

    public HomeListAdapter(List<BillModel> data) {
        mData = data == null ? new ArrayList<BillModel>() : data;
    }

    public void setData(@NonNull List<BillModel> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_bill, parent, false);
        return new ViewHolder(v);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mData != null) {
            BillModel bill = mData.get(position);
            holder.itemView.setSelected(true);
            holder.imgIcon.setImageResource(bill.getCategoryIconRes());
            holder.tvDate.setText(FormatUtils.dateToText(bill.getDate().getTime()));
            if (!TextUtils.isEmpty(bill.getDescription())) {
                holder.tvDescription.setText(bill.getDescription());
            } else if (!TextUtils.isEmpty(bill.getTagName())) {
                holder.tvDescription.setText(bill.getTagName());
            } else {
                holder.tvDescription.setText(R.string.description_holder);
            }
            holder.tvPrice.setText(String.format(
                    MyApplication.getStaticContext().getString(bill.isIncome() ? R.string.income_money : R.string.global_CNY_ZH),
                    FormatUtils.moneyText(bill.getPrice())));
            holder.itemView.setTag(position);
            holder.btnDelete.setTag(position);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public long getItemId(int position) {
        if (mData == null || position >= mData.size()) {
            return super.getItemId(position);
        }
        return mData.get(position).getId();
    }

    public void setItemClickListener(ItemClickListener listener) {
        mListener = listener;
    }

    public interface ItemClickListener {
        /**
         * 使用{@link android.support.v7.widget.RecyclerView.Adapter#getItemId(int)}来通过position获取billId */
        void onItemClick(int position);
        void onMenuClick(View view);
    }

    @NonNull
    public BillModel remove(int position) {
        BillModel billModel = mData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mData.size());
        return billModel;
    }

    public void insert(int position, @NonNull BillModel billModel) {
        mData.add(position, billModel);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, mData.size());
    }


}