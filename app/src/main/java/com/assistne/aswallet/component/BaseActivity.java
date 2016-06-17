package com.assistne.aswallet.component;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.assistne.aswallet.database.RealmDelegate;

/**
 * 做一些全局性的工作
 * Created by assistne on 16/5/24.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        // 启动Realm
        RealmDelegate.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 关闭Realm
        RealmDelegate.close();
    }

    public void hideSoftKeyBoard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
