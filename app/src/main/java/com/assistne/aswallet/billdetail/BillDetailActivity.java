package com.assistne.aswallet.billdetail;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.Toast;

import com.assistne.aswallet.R;
import com.assistne.aswallet.component.BaseActivity;
import com.assistne.aswallet.component.KeyboardFragment;
import com.assistne.aswallet.database.bean.Category;
import com.assistne.aswallet.model.BillModel;
import com.assistne.aswallet.model.CategoryModel;
import com.assistne.aswallet.model.TagModel;
import com.assistne.aswallet.tools.FormatUtils;
import com.assistne.aswallet.tools.PreCondition;
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
    /** 标记键盘输入小数点状态 */
    private boolean mKeyBoardDotFlag;

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
            // 类别列表显示了出来, 那么直接隐藏
            for (int i = 0; i < getFragmentManager().getBackStackEntryCount(); i++) {
                getFragmentManager().popBackStack();
            }
            /** 对应下面, 把背景色显示出来 */
            mInfoSpan.animate().setDuration(400).setInterpolator(new AccelerateInterpolator()).translationY(0).start();
            mListCategory.animate().alpha(0).setDuration(400);
        } else if (!mBillInfoFragment.onBackPressed()){
            // 主界面显示的时候, 看看Fragment要不要处理返回点击事件
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
        if (mAdapter.getItemCount() == 0) {
            mPresenter.getCategory();
        }
    }

    public void chooseIncome() {
        /** 9是默认里面的收入类别 */
        mBillInfoFragment.setCategory(mPresenter.getCategory(Category.Type.INCOME));
    }

    public void chooseExpense() {
        BillModel billModel = (BillModel) PreCondition.checkNotNull(mBillInfoFragment.getBill());
        if (billModel.getCategoryId() > 0 && billModel.getCategoryId() != Category.Type.INCOME) {/** 9是默认里面的收入类别 */
            mBillInfoFragment.setCategory(mPresenter.getCategory(billModel.getCategoryId()));
        } else  {
            mBillInfoFragment.setCategory(mPresenter.getDefaultCategory());
        }
    }

    /** 长按, 仅处理删除按钮, 清空数字 */
    public boolean longClickKeyboard(int flag) {
        switch (flag) {
            case KeyboardFragment.Flag.OPR_DEL:
                mBillInfoFragment.setPriceText(String.valueOf(0));
                return true;
            default:
                return false;
        }
    }
    /** 点击数字键盘 */
    public void clickKeyboard(int flag) {
        float priceF = FormatUtils.textToMoney(mBillInfoFragment.getPriceText().toString());
        if (priceF < 1000000) {// 可以输入的最大数值
            if (KeyboardFragment.Flag.NUM_ZERO <= flag && flag <= KeyboardFragment.Flag.NUM_NINE) {
                /** 数字 */
                if (mKeyBoardDotFlag) {// 小数点处于激活状态, 覆盖小数点后一位
                    priceF = (int)priceF + ((float)flag/10);
                } else {
                    priceF = priceF * 10 + flag;
                }
            } else {
                /** 小数点或者删除 */
                switch (flag) {
                    case KeyboardFragment.Flag.OPR_DEL:// 删除
                        mKeyBoardDotFlag = false;
                        if (priceF - (int)priceF > 0) {// 有小数位清除小数位
                            priceF = (float) Math.floor(priceF);
                        } else {// 没有小数位直接清除个位
                            priceF = (int)priceF/10;
                        }
                        break;
                    default:// 小数点
                        mKeyBoardDotFlag = true;
                }
            }
            mBillInfoFragment.setPriceText(FormatUtils.moneyText(priceF));
        } else {
            Toast.makeText(BillDetailActivity.this, R.string.msg_price_overflow, Toast.LENGTH_SHORT).show();
        }
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
    public void selectCategory(long categoryId) {
        mBillInfoFragment.setCategory(mPresenter.getCategory(categoryId));
    }

    @Override
    public void showTag(List<TagModel> tagList) {
        mBillInfoFragment.showTagSpan(tagList);
    }

    public void getShowTag(int type) {
        mPresenter.getTag(type);
    }
}
