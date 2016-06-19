package com.assistne.aswallet.category;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.assistne.aswallet.R;
import com.assistne.aswallet.component.BaseActivity;
import com.assistne.aswallet.database.PrimaryKeyFactory;
import com.assistne.aswallet.database.bean.Category;
import com.assistne.aswallet.model.CategoryModel;
import com.assistne.aswallet.tools.DensityTool;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 编辑类别
 * Created by assistne on 16/6/15.
 */
public class EditCategoryActivity extends BaseActivity implements EditCatMvp.View {

    @Bind(R.id.edit_category_span_container) CoordinatorLayout mContainer;
    @Bind(R.id.edit_category_rcv) RecyclerView mRecyclerView;
    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.edit_category_span_add) ViewGroup mAddSpan;
    @Bind(R.id.edit_category_edt_name) MaterialEditText mNameEdt;

    private EditCatMvp.Presenter mPresenter;
    private EditCategoryAdapter mAdapter;
    private boolean mIsAddingCat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);
        ButterKnife.bind(this);

        mPresenter = new EditCatPresenter(this);
        initToolbar();
        initRecyclerView();
    }

    private void initRecyclerView() {
        mAdapter = new EditCategoryAdapter();
        mAdapter.setOnItemClickListener(new EditCategoryAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyBoard();
                EditCategoryAdapter.Holder viewHolder = (EditCategoryAdapter.Holder)mRecyclerView.getChildViewHolder(v);
                CheckBox checkBox = viewHolder.selectCbx;
                checkBox.setChecked(!checkBox.isChecked());
            }

            @Override
            public boolean onLongClick(View v) {
                hideSoftKeyBoard();
                if (!mAdapter.isEditing()) {
                    hideAddSpan();
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
        mToolbar.inflateMenu(R.menu.edit_cat_toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit_category_menu_delete:
                        if (mAdapter.hasSelected()) {
                            // TODO: 16/6/16 删除已经关联Category会有问题, 暂时不要从数据库删除
                            mPresenter.softDeleteCategory(mAdapter.getSelectedList());
                        } else {
                            Toast.makeText(EditCategoryActivity.this, R.string.alert_cat_select_nothing, Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    case R.id.edit_category_menu_add:
                        exitEditMode();
                        showAddSpan();
                        return true;
                    case R.id.edit_category_menu_done:
                        int nameLength = mNameEdt.getText().length();
                        if (nameLength > 0 && nameLength <= 10) {
                            mPresenter.addOrUpdateCategory(getCategoryModel());
                        } else {
                            Toast.makeText(EditCategoryActivity.this, R.string.alert_cat_name_length_illegal, Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    default:
                        return false;
                }
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
        mAdapter.insert(0, categoryModel);
        if (((LinearLayoutManager)mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition() == 0) {
            mRecyclerView.scrollToPosition(0);
        }
        Snackbar.make(mContainer, R.string.msg_success_add_cat, Snackbar.LENGTH_SHORT).show();
        mNameEdt.setText("");
    }

    @Override
    public void removeCategory(@NonNull List<Integer> positionList) {
        mAdapter.remove(positionList);
        exitEditMode();
        Snackbar.make(mContainer, R.string.msg_success_delete_cat, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (mAdapter.isEditing() || mIsAddingCat) {
            exitEditMode();
            hideAddSpan();
        } else {
            super.onBackPressed();
        }
    }

    private void exitEditMode() {
        if (mAdapter.isEditing()) {
            mToolbar.getMenu().clear();
            mToolbar.inflateMenu(R.menu.edit_cat_toolbar);
            mAdapter.exitEditMode();
        }
    }

    private void enterEditMode() {
        if (!mAdapter.isEditing()) {
            mAdapter.enterEditMode();
            mToolbar.getMenu().clear();
            mToolbar.inflateMenu(R.menu.edit_cat_toolbar_delete);
        }
    }

    private void showAddSpan() {
        if (!mIsAddingCat) {
            mIsAddingCat = true;
            mAddSpan.setAlpha(0f);
            mAddSpan.setVisibility(View.VISIBLE);
            AnimatorSet set = new AnimatorSet();
            Animator revealAni = ViewAnimationUtils.createCircularReveal(
                    mAddSpan, mAddSpan.getWidth()/2, mAddSpan.getHeight()/2,
                    0, mAddSpan.getWidth()/2);
            Animator alpha = ObjectAnimator.ofFloat(mAddSpan, "alpha", 0, 1);
            Animator translation = ObjectAnimator.ofFloat(mRecyclerView, "translationY",
                    0, mAddSpan.getHeight() + DensityTool.dpToPx(16));
            set.playTogether(revealAni, alpha, translation);
            set.start();

            mToolbar.getMenu().clear();
            mToolbar.inflateMenu(R.menu.edit_cat_toolbar_done);
        }
    }

    private void hideAddSpan() {
        if (mIsAddingCat) {
            mIsAddingCat = false;
            AnimatorSet set = new AnimatorSet();
            Animator revealAni = ViewAnimationUtils.createCircularReveal(
                    mAddSpan, mAddSpan.getWidth()/2, mAddSpan.getHeight()/2,
                    mAddSpan.getWidth()/2, 0);
            revealAni.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mAddSpan.setVisibility(View.INVISIBLE);
                }
            });
            Animator translation = ObjectAnimator.ofFloat(mRecyclerView, "translationY",
                    0);
            set.playSequentially(revealAni, translation);
            set.start();
            mToolbar.getMenu().clear();
            mToolbar.inflateMenu(R.menu.edit_cat_toolbar);
        }
    }

    private CategoryModel getCategoryModel() {
        CategoryModel model = new CategoryModel();
        // 分配ID, 否则覆盖
        model.setId(PrimaryKeyFactory.nextCategoryKey());
        model.setName(mNameEdt.getText().toString());
        model.setActivate(true);
        model.setType(Category.TYPE_EXPENSE);
        model.setIconType(Category.Type.OTHER);
        return model;
    }
}
