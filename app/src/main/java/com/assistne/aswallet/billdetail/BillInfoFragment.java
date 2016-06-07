package com.assistne.aswallet.billdetail;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
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
import com.assistne.aswallet.component.BackPressHandler;
import com.assistne.aswallet.component.MyApplication;
import com.assistne.aswallet.database.bean.Bill;
import com.assistne.aswallet.model.BillModel;
import com.assistne.aswallet.model.CategoryModel;
import com.assistne.aswallet.model.TagModel;
import com.assistne.aswallet.tools.DensityTool;
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
public class BillInfoFragment extends Fragment implements View.OnClickListener, BackPressHandler{
    private static final String KEY_BILL_MODEL = "KEY_BILL_MODEL";

    @Bind(R.id.bill_info_span_type) ViewGroup mTypeSpan;
    @Bind(R.id.bill_info_btn_income) Button mIncomeBtn;
    @Bind(R.id.bill_info_division) View mDivision;
    @Bind(R.id.bill_info_btn_expense) Button mExpenseBtn;
    @Bind(R.id.bill_info_text_price) TextView mPriceTxt;
    @Bind(R.id.bill_info_txt_price_mirror) TextView mPriceTxtMirror;
    @Bind(R.id.bill_info_text_currency) TextView mCurrencyTxt;
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
        mIsTagShow = false;
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
                getTextMoveAni().start();
//                if (mIsTagShow) {
//                    hideTagSpan();
//                } else {
//                    ((BillDetailActivity)getActivity()).getShowTag();
//                }
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
            AnimatorSet moveTextAni = getTextMoveAni();
            moveTextAni.playTogether(anim);
            moveTextAni.start();
            if (mTagSpan.getChildCount() != tagList.size()) {
                FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMarginStart(0);
                layoutParams.setMarginEnd(50);
                layoutParams.setMargins(0, 0, 50, 20);
                layoutParams.flexShrink = 0;
                View.OnClickListener onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Object formerView = mTagSpan.getTag();
                        if (formerView != null && formerView instanceof TextView) {
                            TextView textView = (TextView)formerView;
                            textView.setSelected(false);
                            textView.setTextColor(Color.WHITE);
                        }
                        mTagSpan.setTag(v);
                        v.setSelected(true);
                        ((TextView)v).setTextColor(ContextCompat.getColor(getActivity(), R.color.green_500));
                    }
                };
                mTagSpan.removeAllViews();
                for (TagModel tagModel : tagList) {
                    TextView view = new TextView(getActivity());
                    view.setTag(tagModel);
                    view.setText(tagModel.getName());
                    view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    view.setTextColor(Color.WHITE);
                    view.setBackgroundResource(R.drawable.selector_tag_bg);
                    view.setPadding(20, 10, 20, 10);
                    view.setLayoutParams(layoutParams);
                    view.setOnClickListener(onClickListener);
                    if (mBillModel.getTagId() == tagModel.getId()) {
                        view.setSelected(true);
                        view.setTextColor(ContextCompat.getColor(getActivity(), R.color.green_500));
                        mTagSpan.setTag(view);
                    }
                    mTagSpan.addView(view);
                }
            }
        } else {
            Toast.makeText(getActivity(), "没有标签~", Toast.LENGTH_SHORT).show();
        }
    }

    private AnimatorSet getTextMoveAni() {
        AnimatorSet set = new AnimatorSet();
        int typeSpanDistance = mBillModel.isIncome() ? 100 : 100 + mExpenseBtn.getLeft() - mIncomeBtn.getLeft();
        AnimatorSet typeSpanSet = new AnimatorSet();
        typeSpanSet.playTogether(
                ObjectAnimator.ofFloat(mTypeSpan, "translationX", -typeSpanDistance),
                ObjectAnimator.ofFloat(mBillModel.isIncome() ? mExpenseBtn : mIncomeBtn, "alpha", 0),
                ObjectAnimator.ofFloat(mDivision, "alpha", 0)
        );
        AnimatorSet priceSet = new AnimatorSet();
        int textSize = 20;// 单位是SP
        float translationY = -(mTypeSpan.getHeight() + mCurrencyTxt.getTop());
        Logger.d("translationY  " + translationY);
        translationY = -200;
        priceSet.playTogether(
                ObjectAnimator.ofFloat(mCurrencyTxt, "textSize", textSize),
                ObjectAnimator.ofFloat(mPriceTxtMirror, "textSize", textSize),
                ObjectAnimator.ofFloat(mCurrencyTxt, "translationY", translationY),
                ObjectAnimator.ofFloat(mPriceTxtMirror, "translationY", translationY)
        );
        priceSet.setDuration(2000);
        set.playTogether(typeSpanSet, priceSet);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mPriceTxtMirror.setTextSize(mPriceTxt.getTextSize());
                mPriceTxtMirror.setText(mPriceTxt.getText());
                mPriceTxtMirror.setVisibility(View.VISIBLE);
                mPriceTxt.setVisibility(View.INVISIBLE);
            }
        });
        return set;
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
        TagModel tagModel = null;
        if (mTagSpan.getTag() != null) {
            tagModel = (TagModel) ((TextView)mTagSpan.getTag()).getTag();
        }
        if (tagModel != null) {
            mBillModel.setTagId(tagModel.getId());
            mBillModel.setTagName(tagModel.getName());
        }
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

    @Override
    public boolean onBackPressed() {
        if (mIsTagShow) {
            hideTagSpan();
            return true;
        }
        return false;
    }
}
