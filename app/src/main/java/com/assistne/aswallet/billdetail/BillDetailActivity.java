package com.assistne.aswallet.billdetail;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;

import com.assistne.aswallet.R;
import com.assistne.aswallet.component.BaseActivity;
import com.assistne.aswallet.component.KeyboardFragment;
import com.assistne.aswallet.database.bean.Category;
import com.assistne.aswallet.model.BillModel;
import com.assistne.aswallet.model.CategoryModel;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Page to show details of a bill
 * Created by assistne on 15/12/24.
 */
public class BillDetailActivity extends BaseActivity implements BillMvp.View, View.OnClickListener {
    public static final String KEY_BILL_ID = "KEY_BILL_ID";

    @Bind(R.id.bill_detail_toolbar) Toolbar mToolbar;
    @Bind(R.id.toolbar_btn_cancel) Button mBtnCancel;
    @Bind(R.id.toolbar_btn_ok) Button mBtnOK;
    @Bind(R.id.bill_detail_list_category) RecyclerView mListCategory;
    @Bind(R.id.bill_detail_frag_info) ViewGroup mInfoSpan;

    private CategoryAdapter mAdapter;

    private KeyboardFragment mKeyboardFragment;
    private BillInfoFragment mBillInfoFragment;

    private BillMvp.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        mBtnCancel.setOnClickListener(this);
        mBtnOK.setOnClickListener(this);
        mPresenter = new BillDetailPresenter(this);
        initRecyclerView();
        initBillInfoFragment();
        initKeyboardFragment();
        getFragmentManager().beginTransaction()
                .add(R.id.bill_detail_frag_info, mBillInfoFragment)
                .add(R.id.bill_detail_frag_keyboard, mKeyboardFragment)
                .commit();
    }

    private void initRecyclerView() {
        mListCategory.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CategoryAdapter();
        mAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onClick(CategoryModel category) {
                Logger.d(category.toString());
                mBillInfoFragment.setCategory(category);
                mBtnCancel.performClick();
            }
        });
        mListCategory.setAdapter(mAdapter);
    }

    private BillModel getBillFromIntent() {
        Bundle bundle = getIntent().getExtras();
        BillModel bill = null;
        if (bundle != null && bundle.containsKey(BillDetailActivity.KEY_BILL_ID)) {
            long billId = bundle.getLong(BillDetailActivity.KEY_BILL_ID, -1);
            if (billId > 0) {
                bill = mPresenter.getBill(billId);
            }
        }
        if (bill == null) {
            bill = new BillModel();
            CategoryModel categoryModel = mPresenter.getDefaultCategory();
            bill.setCategoryId(categoryModel.getId());
            bill.setCategoryName(categoryModel.getName());
        }
        Logger.d(bill.toString());
        return bill;
    }

    private void initBillInfoFragment() {
        BillModel bill = getBillFromIntent();
        mBillInfoFragment = BillInfoFragment.newInstance(bill);
    }

    private void initKeyboardFragment() {
        mKeyboardFragment = new KeyboardFragment();
        mKeyboardFragment.setCallback(new KeyboardFragment.ItemClickListener() {
            @Override
            public void onKeyboardClick(int flag) {
                clickKeyboard(flag);
            }

            @Override
            public boolean onKeyboardLongClick(int flag) {
                return flag == KeyboardFragment.Flag.OPR_DEL && longClickKeyboard(flag);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_btn_cancel:
                onBackPressed();
                break;
            case R.id.toolbar_btn_ok:
                mPresenter.updateBill(mBillInfoFragment.getBill());
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            for (int i = 0; i < getFragmentManager().getBackStackEntryCount(); i++) {
                getFragmentManager().popBackStack();
            }
            /** 对应下面, 把背景色显示出来 */
            mInfoSpan.animate().setDuration(400).setInterpolator(new AccelerateInterpolator()).translationY(0).start();
            mListCategory.animate().alpha(0).setDuration(400);
        } else {
            super.onBackPressed();
        }
    }

    public void showCategoryList() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                FragmentTransaction.TRANSIT_NONE, R.animator.fragment_slide_out_to_bottom,
                R.animator.fragment_slide_in_from_bottom, FragmentTransaction.TRANSIT_NONE)
                .remove(mKeyboardFragment)
                .addToBackStack(null)
                .commit();
        transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                FragmentTransaction.TRANSIT_NONE , R.animator.fragment_slide_out_to_top,
                R.animator.fragment_slide_in_from_top, FragmentTransaction.TRANSIT_NONE)
                .remove(mBillInfoFragment)
                .addToBackStack(null)
                .commit();

        mListCategory.animate().alpha(1).setDuration(400);
        /** 这里是由于要产生阴影所以背景色不能是透明, 所以这里要手动把背景色跟着Fragment向上滑动 */
        mInfoSpan.animate().setDuration(400).setInterpolator(new AccelerateInterpolator()).translationY(-mInfoSpan.getHeight()).start();
        Logger.d("size " + mAdapter.getItemCount());
        if (mAdapter.getItemCount() == 0) {
            Logger.d("get cat");
            mPresenter.getCategory();
        }
    }

    public boolean longClickKeyboard(int flag) {
        switch (flag) {
            case KeyboardFragment.Flag.OPR_DEL:
                mBillInfoFragment.setPriceText(String.valueOf(0));
                return true;
            default:
                return false;
        }
    }
    public void clickKeyboard(int flag) {
        CharSequence currentPrice = mBillInfoFragment.getPriceText();
        String result;
        if (KeyboardFragment.Flag.NUM_ZERO <= flag && flag <= KeyboardFragment.Flag.NUM_NINE) {
            try {
                result = String.valueOf(Float.valueOf(currentPrice.toString()) * 10 + flag);
            } catch (NumberFormatException e) {
                result = currentPrice.toString() + flag;
            }
        } else {
            switch (flag) {
                case KeyboardFragment.Flag.OPR_DIV:
                    result = "0";
                    break;
                case KeyboardFragment.Flag.OPR_DEL:
                    if (currentPrice.length() <= 1) {
                        result = "0";
                    } else {
                        result = currentPrice.subSequence(0, currentPrice.length() - 1).toString();
                    }
                    break;
                default:
                    result = "0";
            }
        }
        mBillInfoFragment.setPriceText(result);
    }

    @Override
    public void exit() {
        finish();
    }

    @Override
    public void showCategory(List<CategoryModel> categoryList) {
        mAdapter.setData(categoryList);
    }

    @Override
    public void selectCategory(CategoryModel category) {

    }
}
