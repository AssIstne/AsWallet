package com.assistne.aswallet.home;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.assistne.aswallet.R;
import com.assistne.aswallet.billdetail.BillDetailActivity;
import com.assistne.aswallet.component.BaseActivity;
import com.assistne.aswallet.model.BillModel;
import com.assistne.aswallet.tools.FormatUtils;
import com.orhanobut.logger.Logger;

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
                Logger.d("bill id " + billId);
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
        setUpItemTouchHelper();
    }


    /**
     * 参考
     * http://nemanjakovacevic.net/blog/english/2016/01/12/recyclerview-swipe-to-delete-no-3rd-party-lib-necessary/
     * */
    private void setUpItemTouchHelper() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // we want to cache these and not allocate anything repeatedly in the onChildDraw method
            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(getResources().getColor(R.color.grey_50));
                xMark = ContextCompat.getDrawable(HomeActivity.this, R.drawable.ic_delete_black_36dp);
                xMarkMargin = (int) HomeActivity.this.getResources().getDimension(R.dimen.material_margin_16);
                initiated = true;
            }

            // not important, we don't want drag & drop
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int swipedPosition = viewHolder.getAdapterPosition();
                HomeListAdapter adapter = (HomeListAdapter)mRecyclerView.getAdapter();
                adapter.remove(swipedPosition);
            }

            // 多次调用
            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.getAdapterPosition() == -1) {
                    // not interested in those
                    return;
                }

                if (!initiated) {
                    init();
                }

                // draw red background
                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();
                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);
                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
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
