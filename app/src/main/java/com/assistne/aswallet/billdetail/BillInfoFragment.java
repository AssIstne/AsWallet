package com.assistne.aswallet.billdetail;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
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
    private float mOriginCurrencyTextSize;
    private View.OnClickListener mTagClickListener;

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
        // 记录一下CNY原来的大小
        mOriginCurrencyTextSize = mCurrencyTxt.getTextSize();
        /**
         * 标签点击事件 */
        mTagClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** 选中的标签视图会存入{@link BillInfoFragment#mTagSpan}中 */
                Object formerView = mTagSpan.getTag();
                // 把旧的标签取消选择
                if (formerView != null && formerView instanceof TextView) {
                    TextView textView = (TextView)formerView;
                    textView.setSelected(false);
                    textView.setTextColor(Color.WHITE);
                }
                // 标签实例存在标签
                TagModel tagModel = (TagModel) v.getTag();
                if (tagModel.getId() == mBillModel.getTagId()) {
                    mTagSpan.setTag(null);
                } else {
                    ((TextView)v).setTextColor(ContextCompat.getColor(getActivity(), R.color.green_500));
                    v.setSelected(true);
                    mTagSpan.setTag(v);
                }
                /** 根据标签选类别 */
                ((BillDetailActivity)getActivity()).selectCategory(tagModel.getCategoryId());
            }
        };
        return root;
    }

    @Override
    public void onClick(View v) {
        hideSoftKeyBoard();
        switch (v.getId()) {
            case R.id.bill_info_btn_expense:
                mBillModel.setType(Bill.TYPE_EXPENSE);
                setExpenseUI();
                ((BillDetailActivity)getActivity()).chooseExpense();
                mCategorySpan.setClickable(true);
                break;
            case R.id.bill_info_btn_income:
                mBillModel.setType(Bill.TYPE_INCOME);
                setIncomeUI();
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
                    ((BillDetailActivity)getActivity()).getShowTag(mBillModel.getType());
                }
                break;
        }
    }

    /**
     * 显示标签面板同时通过动画移动金额文字
     * */
    public void showTagSpan(List<TagModel> tagList) {
        if (tagList != null && tagList.size() > 0) {
            mIsTagShow = true;
            int cx = mTagSpan.getLeft();
            int cy = (mTagSpan.getTop() + mTagSpan.getBottom()) / 2;
            int finalRadius = Math.max(mTagSpan.getWidth(), mTagSpan.getHeight());
            // circle reveal效果
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(mTagSpan, cx, cy, 0, finalRadius);
            mTagSpan.setVisibility(View.VISIBLE);
            // 移动金额文本
            AnimatorSet moveTextAni = getTextMoveAni();
            moveTextAni.playTogether(anim);
            moveTextAni.start();
            if (mTagSpan.getChildCount() != tagList.size()) {
                FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                /** 一定要这样重复设置
                 * {@link com.google.android.flexbox.FlexboxLayout.LayoutParams#setMargins(int, int, int, int)}
                 * 中的top和bottom才会生效*/
                layoutParams.setMarginStart(0);
                layoutParams.setMarginEnd(50);
                layoutParams.setMargins(0, 0, 50, 20);
                // 重新添加标签视图
                mTagSpan.removeAllViews();
                for (TagModel tagModel : tagList) {
                    TextView view = new TextView(getActivity());
                    // 把实例放入视图中
                    view.setTag(tagModel);
                    view.setText(tagModel.getName());
                    view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    view.setTextColor(Color.WHITE);
                    view.setBackgroundResource(R.drawable.selector_tag_bg);
                    view.setPadding(20, 10, 20, 10);
                    view.setLayoutParams(layoutParams);
                    view.setOnClickListener(mTagClickListener);
                    if (mBillModel.getTagId() == tagModel.getId()) {// 显示已经被选中的标签
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
        int typeSpanDistance;
        if (mPriceTxt.getText().length() > 5) {
            // 金额太大, 不居中
            typeSpanDistance = mBillModel.isIncome() ? 0 : -mExpenseBtn.getWidth();
        } else {
            // 要显示类别放到中间, 不显示的隐藏
            typeSpanDistance = mBillModel.isIncome() ? mExpenseBtn.getWidth()/2 : -mExpenseBtn.getWidth()/2;
        }
        AnimatorSet typeSpanSet = new AnimatorSet();
        typeSpanSet.playTogether(
                ObjectAnimator.ofFloat(mTypeSpan, "translationX", typeSpanDistance),
                ObjectAnimator.ofFloat(mBillModel.isIncome() ? mExpenseBtn : mIncomeBtn, "alpha", 0),
                ObjectAnimator.ofFloat(mDivision, "alpha", 0)
        );
        AnimatorSet priceSet = new AnimatorSet();
        // 缩小文本
        float textSize = DensityTool.spToPx(25);// 单位是PX
        ValueAnimator currencyTextSizeAni = ValueAnimator.ofFloat(mOriginCurrencyTextSize, textSize);
        currencyTextSizeAni.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrencyTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) animation.getAnimatedValue());
            }
        });
        ValueAnimator priceTextSizeAni = ValueAnimator.ofFloat(mPriceTxt.getTextSize(), textSize);
        priceTextSizeAni.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPriceTxtMirror.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) animation.getAnimatedValue());
            }
        });
        // 计算从原来位置运动到跟类别一条水平线上的位移
        float translationY = -(mTypeSpan.getHeight()/2 + mCurrencyTxt.getTop() + mCurrencyTxt.getHeight()/2);
        priceSet.playTogether(
                currencyTextSizeAni,
                priceTextSizeAni,
                ObjectAnimator.ofFloat(mCurrencyTxt, "translationY", translationY),
                ObjectAnimator.ofFloat(mPriceTxtMirror, "translationY", translationY)
        );
        set.playTogether(typeSpanSet, priceSet);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                /** 制造金额的镜像用来显示动画 */
                mPriceTxtMirror.setTextSize(mPriceTxt.getTextSize());
                mPriceTxtMirror.setText(mPriceTxt.getText());
                mPriceTxtMirror.setVisibility(View.VISIBLE);
                mPriceTxt.setVisibility(View.INVISIBLE);
            }
        });

        return set;
    }

    /**
     * 隐藏标签面板同时通过动画移动金额文字
     * {@link #showTagSpan(List)}的逆操作
     * */
    private void hideTagSpan() {
        mIsTagShow = false;
        int cx = mTagSpan.getLeft();
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
        AnimatorSet restoreAni = getTextRestoreAni();
        restoreAni.playTogether(anim);
        restoreAni.start();
    }

    /** 对应{@link #getTextMoveAni()}*/
    private AnimatorSet getTextRestoreAni() {
        AnimatorSet set = new AnimatorSet();
        AnimatorSet typeSpanSet = new AnimatorSet();
        typeSpanSet.playTogether(
                ObjectAnimator.ofFloat(mTypeSpan, "translationX", 0),
                ObjectAnimator.ofFloat(mBillModel.isIncome() ? mExpenseBtn : mIncomeBtn, "alpha", 1),
                ObjectAnimator.ofFloat(mDivision, "alpha", 1)
        );
        AnimatorSet priceSet = new AnimatorSet();
        ValueAnimator currencyTextSizeAni = ValueAnimator.ofFloat(mCurrencyTxt.getTextSize(), mOriginCurrencyTextSize);
        currencyTextSizeAni.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrencyTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) animation.getAnimatedValue());
            }
        });
        ValueAnimator priceTextSizeAni = ValueAnimator.ofFloat(mPriceTxtMirror.getTextSize(), mPriceTxt.getTextSize());
        priceTextSizeAni.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPriceTxtMirror.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) animation.getAnimatedValue());
            }
        });
        priceSet.playTogether(
                currencyTextSizeAni,
                priceTextSizeAni,
                ObjectAnimator.ofFloat(mCurrencyTxt, "translationY", 0),
                ObjectAnimator.ofFloat(mPriceTxtMirror, "translationY", 0)
        );
        set.playTogether(typeSpanSet, priceSet);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mPriceTxtMirror.setVisibility(View.INVISIBLE);
                mPriceTxt.setVisibility(View.VISIBLE);
            }
        });

        return set;
    }

    private void setIncomeUI() {
        mExpenseBtn.setTypeface(null, Typeface.NORMAL);
        mIncomeBtn.setTypeface(null, Typeface.BOLD);
        mExpenseBtn.setActivated(false);
        mIncomeBtn.setActivated(true);
    }

    private void setExpenseUI(){
        mExpenseBtn.setTypeface(null, Typeface.BOLD);
        mIncomeBtn.setTypeface(null, Typeface.NORMAL);
        mIncomeBtn.setActivated(false);
        mExpenseBtn.setActivated(true);
    }

    /**
     * 修改金额, 会影响{@link #mBillModel} */
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
                setExpenseUI();
            } else {
                setIncomeUI();
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

    /**
     * 修改类别, 会影响{@link #mBillModel} */
    public void setCategory(@NonNull CategoryModel category) {
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
