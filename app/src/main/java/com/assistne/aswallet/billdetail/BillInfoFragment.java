package com.assistne.aswallet.billdetail;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.assistne.aswallet.R;
import com.assistne.aswallet.database.bean.Bill;
import com.assistne.aswallet.model.BillModel;
import com.assistne.aswallet.model.CategoryModel;
import com.assistne.aswallet.model.TagModel;
import com.assistne.aswallet.tools.FormatUtils;
import com.google.android.flexbox.FlexboxLayout;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Fragment to show information of a bill
 * Created by assistne on 16/2/20.
 */
public class BillInfoFragment extends Fragment implements View.OnClickListener{
    private static final String KEY_BILL_MODEL = "KEY_BILL_MODEL";

    @Bind(R.id.bill_info_btn_income) Button mIncomeBtn;
    @Bind(R.id.bill_info_btn_expense) Button mExpenseBtn;
    @Bind(R.id.bill_info_text_price) TextView mPriceTxt;
    @Bind(R.id.bill_info_edit_description) EditText mDescriptionTxt;
    @Bind(R.id.bill_info_text_category) TextView mCategoryTxt;
    @Bind(R.id.bill_info_vg_category) ViewGroup mCategorySpan;
    @Bind(R.id.bill_info_img_tag) View mTagBtn;
    @Bind(R.id.bill_detail_span_tag) FlexboxLayout mTagSpan;

    private BillModel mBillModel;
    private boolean mIsTagShow;

    public static BillInfoFragment newInstance(BillModel bill) {
        BillInfoFragment fragment = new BillInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_BILL_MODEL, bill);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bill_info, container, false);
        ButterKnife.bind(this, root);

        root.setOnClickListener(this);
        mPriceTxt.setMaxLines(1);
        mExpenseBtn.setOnClickListener(this);
        mIncomeBtn.setOnClickListener(this);
        mCategorySpan.setOnClickListener(this);
        mTagBtn.setOnClickListener(this);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mBillModel = bundle.getParcelable(KEY_BILL_MODEL);
        }
        if (mBillModel == null) {
            mBillModel = new BillModel();
        }
        showBill(mBillModel);
        return root;
    }

    @Override
    public void onClick(View v) {
        hideSoftKeyBoard();
        switch (v.getId()) {
            case R.id.bill_info_btn_expense:
                mBillModel.setType(Bill.TYPE_EXPENSE);
                setExpense();
                ((BillDetailActivity)getActivity()).chooseExpense();
                mCategorySpan.setClickable(true);
                break;
            case R.id.bill_info_btn_income:
                mBillModel.setType(Bill.TYPE_INCOME);
                setIncome();
                ((BillDetailActivity)getActivity()).chooseIncome();
                mCategorySpan.setClickable(false);
                break;
            case R.id.bill_info_vg_category:
                ((BillDetailActivity)getActivity()).showCategoryList();
                break;
            case R.id.bill_info_img_tag:
                if (mIsTagShow) {
                    hideTagSpan();
                } else {
                    ((BillDetailActivity)getActivity()).getShowTag();
                }
                break;
        }
    }

    public void showTagSpan(List<TagModel> tagList) {
        if (tagList != null && tagList.size() > 0) {
            mIsTagShow = true;
            int cx = mTagSpan.getRight();
            int cy = (mTagSpan.getTop() + mTagSpan.getBottom()) / 2;
            int finalRadius = Math.max(mTagSpan.getWidth(), mTagSpan.getHeight());
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(mTagSpan, cx, cy, 0, finalRadius);
            mTagSpan.setVisibility(View.VISIBLE);
            anim.start();

            // TODO: 16/6/6 增加标签视图
            for (TagModel tagModel : tagList) {
                TextView view = new TextView(getActivity());
                view.setText(tagModel.getName());
                FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.order = -1;
                lp.flexGrow = 2;
                view.setLayoutParams(lp);
                mTagSpan.addView(view);
            }
        } else {
            Toast.makeText(getActivity(), "没有标签~", Toast.LENGTH_SHORT).show();
        }
    }

    private void hideTagSpan() {
        mIsTagShow = false;
        int cx = mTagSpan.getRight();
        int cy = (mTagSpan.getTop() + mTagSpan.getBottom()) / 2;
        int initialRadius = mTagSpan.getWidth();
        Animator anim = ViewAnimationUtils.createCircularReveal(mTagSpan, cx, cy, initialRadius, 0);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mTagSpan.setVisibility(View.INVISIBLE);
            }
        });
        anim.start();

    }


    private void setIncome() {
        mExpenseBtn.setTypeface(null, Typeface.NORMAL);
        mIncomeBtn.setTypeface(null, Typeface.BOLD);
        mExpenseBtn.setActivated(false);
        mIncomeBtn.setActivated(true);
    }

    private void setExpense(){
        mExpenseBtn.setTypeface(null, Typeface.BOLD);
        mIncomeBtn.setTypeface(null, Typeface.NORMAL);
        mIncomeBtn.setActivated(false);
        mExpenseBtn.setActivated(true);
    }

    public void setPriceText(String content) {
        mPriceTxt.setText(content);
        mBillModel.setPrice(FormatUtils.textToMoney(content));
    }

    public CharSequence getPriceText() {
        return mPriceTxt.getText();
    }

    private void showBill(@Nullable BillModel bill) {
        if (bill != null) {
            mPriceTxt.setText(FormatUtils.moneyText(bill.getPrice()));
            mCategoryTxt.setText(mBillModel.getCategoryName());
            mDescriptionTxt.setText(bill.getDescription());
            if (!bill.isIncome()) {
                setExpense();
            } else {
                setIncome();
            }
        }
    }

    public BillModel getBill() {
        mBillModel.setDescription(mDescriptionTxt.getText().toString());
        return mBillModel;
    }

    public void setCategory(@NonNull CategoryModel category) {
        Logger.d(category.toString());
        mBillModel.setCategoryName(category.getName());
        mBillModel.setCategoryId(category.getId());
        mCategoryTxt.setText(category.getName());
    }

    private void hideSoftKeyBoard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
