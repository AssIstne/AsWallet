package com.assistne.aswallet.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.assistne.aswallet.R;
import com.assistne.aswallet.billdetail.BillDetailActivity;
import com.assistne.aswallet.component.BaseActivity;
import com.assistne.aswallet.model.BillModel;
import com.assistne.aswallet.tools.FormatUtils;
import com.orhanobut.logger.Logger;
import com.tubb.smrv.SwipeMenuRecyclerView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity implements HomeMvp.View {
    @Bind(R.id.home_btn_fab) FloatingActionButton mFab;
    @Bind(R.id.home_list) RecyclerView mRecyclerView;
    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.home_span_drawer) DrawerLayout mDrawer;
    @Bind(R.id.home_span_navigation) NavigationView mNavigationSpan;

    private TextView mExpenseTxt;
    private TextView mIncomeTxt;
    private TextView mDifferenceTxt;
    private View mGuideLine;

    private HomeListAdapter mAdapter;
    private HomeMvp.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        View header = mNavigationSpan.getHeaderView(0);
        mExpenseTxt = (TextView) header.findViewById(R.id.drawer_expense);
        mIncomeTxt = (TextView) header.findViewById(R.id.drawer_income);
        mDifferenceTxt = (TextView) header.findViewById(R.id.drawer_difference);
        mGuideLine = header.findViewById(R.id.drawer_guide_line);
        setSupportActionBar(mToolbar);
        initMembers();
    }

    private void initMembers() {
        mPresenter = new HomePresenter(this);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.openDrawer(GravityCompat.START);
            }
        });
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, BillDetailActivity.class));
            }
        });
        initList();
    }

    /** 初始化列表 */
    private void initList() {
        mAdapter = new HomeListAdapter();
        mAdapter.setItemClickListener(new HomeListAdapter.ItemClickListener() {
            @Override
            public void onClick(long billId) {
                Intent intent = new Intent(HomeActivity.this, BillDetailActivity.class);
                intent.putExtra(BillDetailActivity.KEY_BILL_ID, billId);
                startActivity(intent);
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        // 增加底线
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(getResources().getDrawable(R.drawable.list_divider_line, null)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public void showBill(List<BillModel> billList) {
        mAdapter.setData(billList);
    }

    @Override
    public void showShortAnalyze(float expense, float income) {
        mExpenseTxt.setText(FormatUtils.moneyText(expense));
        mIncomeTxt.setText(FormatUtils.moneyText(income));
        if (expense == income) { // 相等时不显示差值
            mGuideLine.setVisibility(View.INVISIBLE);
            mDifferenceTxt.setVisibility(View.INVISIBLE);
        } else {
            mGuideLine.setVisibility(View.VISIBLE);
            mDifferenceTxt.setVisibility(View.VISIBLE);
            mDifferenceTxt.setText(getResources().getString(
                    expense > income ? R.string.drawer_negative_diff : R.string.drawer_positive_diff,
                    FormatUtils.moneyText(Math.abs(income - expense))));
        }
        /** 根据文本长度调整{@link #mGuideLine}长度, 3个TextView尾端对齐{@link #mGuideLine} */
        RelativeLayout.LayoutParams lineParams = (RelativeLayout.LayoutParams) mGuideLine.getLayoutParams();
        // 动态设置长度
        TextView target = expense > income ? mExpenseTxt : mIncomeTxt;
        target.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        lineParams.width = target.getMeasuredWidth();
    }

    @Override
    public void removeBillFromList(int position) {

    }
}
