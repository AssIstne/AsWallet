package com.assistne.aswallet.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.assistne.aswallet.R;
import com.assistne.aswallet.billdetail.BillDetailActivity;
import com.assistne.aswallet.model.BillModel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements HomeMvp.View {
    @Bind(R.id.home_btn_fab) FloatingActionButton mFab;
    @Bind(R.id.home_list) RecyclerView mRecyclerView;

    private HomeListAdapter mAdapter;
    private HomeMvp.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initMembers();
    }

    private void initMembers() {
        mPresenter = new HomePresenter(this);
        mAdapter = new HomeListAdapter();
        mAdapter.setItemClickListener(new HomeListAdapter.ItemClickListener() {
            @Override
            public void onClick(int billId) {
                Intent intent = new Intent(HomeActivity.this, BillDetailActivity.class);
                intent.putExtra(BillDetailActivity.KEY_BILL_ID, billId);
                HomeActivity.this.startActivity(intent);
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, BillDetailActivity.class));
            }
        });
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
    public void removeBillFromList(int position) {

    }
}
