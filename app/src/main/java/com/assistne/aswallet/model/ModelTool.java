package com.assistne.aswallet.model;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.assistne.aswallet.R;
import com.assistne.aswallet.database.bean.Bill;
import com.assistne.aswallet.database.bean.Category;

import java.util.ArrayList;
import java.util.List;

import com.assistne.aswallet.database.bean.Tag;
import com.assistne.aswallet.tools.PreCondition;

/**
 * 数据库类和视图展示类之间的转换工具
 * Created by assistne on 16/5/21.
 */
public class ModelTool {
    public static BillModel convert(@NonNull Bill bill) {
        BillModel model = new BillModel();
        model.setId(bill.getId());
        Category category = bill.getCategory();
        PreCondition.checkNotNull(category);
        model.setCategoryName(category.getName());
        model.setCategoryId(category.getId());
        model.setDescription(bill.getDescription());
        model.setPrice(bill.getPrice());
        model.setDate(bill.getDate());
        model.setType(bill.getType());
        model.setCategoryIconRes(convertIconType(category.getIconType()));
        if (bill.getTag() != null) {
            Tag tag = bill.getTag();
            model.setTagId(tag.getId());
            model.setTagName(tag.getName());
        }
        return model;
    }

    public static CategoryModel convert(@NonNull Category category) {
        CategoryModel model = new CategoryModel();
        model.setId(category.getId());
        model.setName(category.getName());
        model.setType(category.getType());
        model.setActivate(category.isActivate());
        model.setIconRes(convertIconType(category.getIconType()));
        return model;
    }

    public static Bill convert(@NonNull BillModel model, @NonNull Category category, @Nullable Tag tag) {
        Bill bill = new Bill();
        bill.setId(model.getId());
        bill.setCategory(category);
        bill.setDescription(model.getDescription());
        bill.setPrice(model.getPrice());
        bill.setDate(model.getDate());
        bill.setType(model.getType());
        if (tag != null) {
            bill.setTag(tag);
        }
        return bill;
    }

    public static List<BillModel> convertBillList(@NonNull List<Bill> billList) {
        List<BillModel> billModelList = new ArrayList<>();
        for (int i = 0; i < billList.size(); i ++) {
            billModelList.add(convert(billList.get(i)));
        }
        return billModelList;
    }

    public static List<CategoryModel> convertCategoryList(@NonNull List<Category> categoryList) {
        List<CategoryModel> res = new ArrayList<>();
        for (Category category : categoryList) {
            res.add(convert(category));
        }
        return res;
    }

    public static TagModel convert(@NonNull Tag tag) {
        TagModel model = new TagModel();
        model.setId(tag.getId());
        Category category = tag.getCategory();
        PreCondition.checkNotNull(category);
        model.setCategoryId(category.getId());
        model.setName(tag.getName());
        return model;
    }

    public static List<TagModel> convertTagList(@NonNull List<Tag> tagList) {
        List<TagModel> res = new ArrayList<>();
        for (Tag tag : tagList) {
            res.add(convert(tag));
        }
        return res;
    }

    @DrawableRes
    private static int convertIconType(int type) {
        switch (type) {
            case 0:
                return R.drawable.ic_food;
            case 1:
                return R.drawable.ic_entertainment;
            case 2:
                return R.drawable.ic_hospital;
            case 3:
                return R.drawable.ic_education;
            case 4:
                return R.drawable.ic_travel;
            case 5:
                return R.drawable.ic_traffic;
            case 6:
                return R.drawable.ic_social;
            case 8:
                return R.drawable.ic_income;
            default:
                return R.drawable.ic_normal;
        }
    }
}
