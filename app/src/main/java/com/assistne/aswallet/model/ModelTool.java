package com.assistne.aswallet.model;

import com.assistne.aswallet.database.bean.Bill;
import com.assistne.aswallet.database.bean.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by assistne on 16/5/21.
 */
public class ModelTool {
    public static BillModel convert(Bill bill) {
        BillModel model = new BillModel();
        model.setId(bill.getId());
        model.setCategoryName(bill.getCategory().getName());
        model.setDescription(bill.getDescription());
        model.setPrice(bill.getPrice());
        model.setDate(bill.getDate());
        model.setType(bill.getType());
        return model;
    }

    public static Bill convert(BillModel model, Category category) {
        Bill bill = new Bill();
        bill.setId(model.getId());
        bill.setCategory(category);
        bill.setDescription(model.getDescription());
        bill.setPrice(model.getPrice());
        bill.setDate(model.getDate());
        bill.setType(model.getType());
        return bill;
    }

    public static List<BillModel> convert(List<Bill> billList) {
        List<BillModel> billModelList = new ArrayList<>();
        for (int i = 0; i < billList.size(); i ++) {
            billModelList.add(convert(billList.get(i)));
        }
        return billModelList;
    }
}
