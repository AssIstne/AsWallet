package com.assistne.aswallet.category;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.assistne.aswallet.R;
import com.assistne.aswallet.component.BaseActivity;

/**
 * 编辑类别
 * Created by assistne on 16/6/15.
 */
public class EditCategoryActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_left);
    }
}
