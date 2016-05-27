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

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private List<BillModel> mData;
    private ItemClickListener mListener;


    public class ViewHolder extends RecyclerView.ViewHolder {
        public int type;
        @Bind(R.id.list_item_img_icon) public ImageView imgIcon;
        @Bind(R.id.list_item_text_date) public TextView tvDate;
        @Bind(R.id.list_item_text_description) public TextView tvDescription;
        @Bind(R.id.list_item_text_price) public TextView tvPrice;
        public ViewHolder(View v, int typeIn) {
            super(v);
            type = typeIn;
            if (typeIn == TYPE_ITEM) {
                ButterKnife.bind(this, v);
                v.setOnClickListener(HomeListAdapter.this);
            } else if (typeIn == TYPE_HEADER) {
//                    v.setOnClickListener(null);
            }
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
        if (viewType == TYPE_ITEM) {
            final View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_bill, parent, false);
            ViewHolder vh = new ViewHolder(v, TYPE_ITEM);
            return vh;
        } else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_bill_header, parent, false);
            return new ViewHolder(v, TYPE_HEADER);
        }

        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mData != null && holder.type == TYPE_ITEM) {
            BillModel bill = mData.get(position-1);
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
        return mData.size() + 1;
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