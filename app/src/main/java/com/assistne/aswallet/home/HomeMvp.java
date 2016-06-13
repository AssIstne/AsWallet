package com.assistne.aswallet.home;

import com.assistne.aswallet.component.BaseMvp;
import com.assistne.aswallet.database.bean.Bill;
import com.assistne.aswallet.model.BillModel;

import java.util.List;

/**
 * Created by assistne on 16/5/19.
 */
public interface HomeMvp {
    interface View {
        void showBill(List<BillModel> billList);
        /**
         * 显示(更新)Drawer中的月分析数据 */
        void showShortAnalyze(float expense, float income);
        void removeBillFromList(int position);
    }

    interface Presenter extends BaseMvp.Presenter {
        void deleteBillFromDataBase();
        BillModel getBill(int id);
    }
}
