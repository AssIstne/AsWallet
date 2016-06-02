package com.assistne.aswallet.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.assistne.aswallet.R;
import com.assistne.aswallet.model.BillModel;
import com.assistne.aswallet.tools.FormatUtils;
import com.orhanobut.logger.Logger;

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

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            v.setOnClickListener(HomeListAdapter.this);
        }
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onClick((long) v.getTag());
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
//                TODO
            holder.imgIcon.setImageResource(R.drawable.ic_local_dining_white_36dp);
            holder.tvDate.setText(FormatUtils.dateToText(bill.getDate().getTime()));
            holder.tvDescription.setText(bill.getDescription());
            holder.tvPrice.setText(String.valueOf(bill.getPrice()));
            holder.itemView.setTag(bill.getId());
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setItemClickListener(ItemClickListener listener) {
        mListener = listener;
    }

    public interface ItemClickListener {
        void onClick(long billId);
    }

    public void remove(int position) {
        Logger.d("size : " + mData.size() + " position : " + position);
        mData.get(position);
        mData.remove(position);
        notifyItemRemoved(position);
    }
}