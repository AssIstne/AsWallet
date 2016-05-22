package com.assistne.aswallet.billdetail;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.assistne.aswallet.R;
import com.assistne.aswallet.database.bean.Bill;
import com.assistne.aswallet.database.bean.Category;
import com.assistne.aswallet.model.BillModel;
import com.orhanobut.logger.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Fragment to show information of a bill
 * Created by assistne on 16/2/20.
 */
public class BillInfoFragment extends Fragment implements View.OnClickListener{
    private static final String KEY_BILL = "KEY_BILL_ID";

    @Bind(R.id.bill_info_btn_income) Button mBtnIncome;
    @Bind(R.id.bill_info_btn_expense) Button mBtnExpense;
    @Bind(R.id.bill_info_text_price) TextView mTextPrice;
    @Bind(R.id.bill_info_edit_description) EditText mEditTextDescription;
    @Bind(R.id.bill_info_text_category) TextView mTextCategory;
    @Bind(R.id.bill_info_vg_category) ViewGroup mVGCategory;

    private BillModel mBill;

    public static BillInfoFragment newInstance(BillModel bill) {
        BillInfoFragment fragment = new BillInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_BILL, bill);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bill_info, container, false);
        ButterKnife.bind(this, root);

        mTextPrice.setMaxLines(1);
        mBtnExpense.setOnClickListener(this);
        mBtnIncome.setOnClickListener(this);
        mVGCategory.setOnClickListener(this);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mBill = bundle.getParcelable(KEY_BILL);
        }
        if (mBill == null) {
            mBill = new BillModel();
        }
        showBill(mBill);
        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bill_info_btn_expense:
                mBill.setType(Bill.TYPE_EXPENSE);
                setExpense();
                break;
            case R.id.bill_info_btn_income:
                mBill.setType(Bill.TYPE_INCOME);
                setIncome();
                break;
            case R.id.bill_info_vg_category:
//                TODO
                ((BillDetailActivity)getActivity()).showCategoryList();
                break;
        }
    }

    private void setIncome() {
        mBtnExpense.setTypeface(null, Typeface.NORMAL);
        mBtnIncome.setTypeface(null, Typeface.BOLD);
        mBtnExpense.setActivated(false);
        mBtnIncome.setActivated(true);
    }

    private void setExpense(){
        mBtnExpense.setTypeface(null, Typeface.BOLD);
        mBtnIncome.setTypeface(null, Typeface.NORMAL);
        mBtnIncome.setActivated(false);
        mBtnExpense.setActivated(true);
    }

    public void setPriceText(String content) {
        mTextPrice.setText(content);
        mBill.setPrice(Float.valueOf(content));
    }

    public CharSequence getPriceText() {
        return mTextPrice.getText();
    }

    private void showBill(@Nullable BillModel bill) {
        if (bill != null) {
            mTextPrice.setText(String.valueOf(bill.getPrice()));
            mTextCategory.setText(mBill.getCategoryName());
            mEditTextDescription.setText(bill.getDescription());
            if (bill.getType() == Bill.TYPE_EXPENSE) {
                setExpense();
            } else {
                setIncome();
            }
        }
    }

    public BillModel getBill() {
        return mBill;
    }

    public void setCategory(@NonNull Category category) {
        mBill.setCategoryName(category.getName());
        mTextCategory.setText(category.getName());
    }

}
