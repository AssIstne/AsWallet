package com.assistne.aswallet.component;

import com.assistne.aswallet.model.Bill;
import com.assistne.aswallet.model.Category;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import org.litepal.LitePalApplication;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

/**
 * Created by assistne on 15/12/24.
 */
public class MyApplication extends LitePalApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init().logLevel(LogLevel.FULL).methodCount(1).hideThreadInfo();

        Connector.getDatabase();
        // TODO: 16/5/19 测试用
        if (DataSupport.count(Category.class) == 0) {
            Category category = new Category("餐饮");
            category.save();
            category = new Category("娱乐");
            category.save();
            category = new Category("医疗");
            category.save();
            category = new Category("教育");
            category.save();
            category = new Category("出行");
            category.save();
            category = new Category("交通");
            category.save();
            category = new Category("社交");
            category.save();
        }

        if (DataSupport.count(Bill.class) == 0) {
            Bill bill = new Bill();
            bill.setCategory(DataSupport.find(Category.class, 2));
            bill.save();
        }

        Category c = DataSupport.find(Category.class, 2);
        Logger.d(c.toString());
        for (Bill bill : DataSupport.findAll(Bill.class)){
            Logger.d("category : "+bill.getCategory());
            if (bill.getCategory() == null) {
                bill.setCategory(c);
                Logger.d("save " + bill.save());
                Logger.d(bill.toString());
            }
        }
        for (Bill bill : DataSupport.findAll(Bill.class)){
            Logger.d(bill.toString());
        }
    }
}
