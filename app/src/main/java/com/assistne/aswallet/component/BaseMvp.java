package com.assistne.aswallet.component;

/**
 * Created by assistne on 16/5/19.
 */
public interface BaseMvp {
    interface View {
        void setPresenter(Presenter presenter);
    }

    interface Presenter {
        void onResume();
    }
}
