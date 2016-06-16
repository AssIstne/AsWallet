package com.assistne.aswallet.category;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.assistne.aswallet.R;
import com.assistne.aswallet.component.BaseActivity;
import com.assistne.aswallet.model.CategoryModel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 编辑类别
 * Created by assistne on 16/6/15.
 */
public class EditCategoryActivity extends BaseActivity implements EditCatMvp.View {

    @Bind(R.id.edit_category_span_container) CoordinatorLayout mContainer;
    @Bind(R.id.edit_category_btn_fab) FloatingActionButton mFAbtn;
    @Bind(R.id.edit_category_rcv) RecyclerView mRecyclerView;
    @Bind(R.id.toolbar) Toolbar mToolbar;

    private EditCatMvp.Presenter mPresenter;
    private CategoryAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);
        ButterKnife.bind(this);

        mPresenter = new EditCatPresenter(this);
        initToolbar();
        initRecyclerView();

        mFAbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.deleteCategory(-1, -1);
            }
        });
    }

    private void initRecyclerView() {
        mAdapter = new CategoryAdapter();
        mAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v) {
                CategoryAdapter.Holder viewHolder = (CategoryAdapter.Holder)mRecyclerView.getChildViewHolder(v);
                CheckBox checkBox = viewHolder.selectCbx;
                checkBox.setChecked(!checkBox.isChecked());
            }

            @Override
            public boolean onLongClick(View v) {
                if (!mAdapter.isEditing()) {
                    enterEditMode();
                    return true;
                }
                return false;
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.edit_category_menu_delete) {
                    if (mAdapter.hasSelected()) {
                        // TODO: 16/6/16 删除已经关联Category会有问题, 暂时不要从数据库删除
                        mPresenter.softDeleteCategory(mAdapter.getSelectedList());
                    } else {
                        Toast.makeText(EditCategoryActivity.this, R.string.alert_cat_select_nothing, Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public void finish() {
        super.finish();
        // TODO: 16/6/16 结束动画选择其他不行?
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
    }

    @Override
    public void showCategoryList(List<CategoryModel> catList) {
        mAdapter.setData(catList);
    }

    @Override
    public void insertCategory(CategoryModel categoryModel) {

    }

    @Override
    public void removeCategory(@NonNull List<Integer> positionList) {
        mAdapter.remove(positionList);
        exitEditMode();
        Snackbar.make(mContainer, R.string.msg_success_delete_cat, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (mAdapter.isEditing()) {
            exitEditMode();
        } else {
            super.onBackPressed();
        }
    }

    private void exitEditMode() {
        mToolbar.getMenu().clear();
        mAdapter.exitEditMode();
    }

    private void enterEditMode() {
        mAdapter.enterEditMode();
        mToolbar.inflateMenu(R.menu.edit_cat_toolbar);
    }
}
