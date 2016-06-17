package com.assistne.aswallet.model;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.assistne.aswallet.R;
import com.assistne.aswallet.database.bean.Bill;
import com.assistne.aswallet.database.bean.Category;
import com.assistne.aswallet.database.bean.Tag;
import com.assistne.aswallet.tools.PreCondition;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库类和视图展示类之间的转换工具
 * Created by assistne on 16/5/21.
 */
public class ModelTool {
    public static BillModel convert(@NonNull Bill bill) {
        BillModel model = new BillModel();
        model.setId(bill.getId());
        Category category = bill.getCategory();
        // TODO: 16/6/16 如果用户自己删除了Category怎么办?
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
        model.setIconType(category.getIconType());
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

    /** 没有设置softDelete, 默认false*/
    public static Category convert(@NonNull CategoryModel model) {
        Category category = new Category();
        category.setId(model.getId());
        category.setName(model.getName());
        category.setActivate(model.isActivate());
        category.setType(model.getType());
        category.setIconType(model.getIconType());
        return category;
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
    public static int convertIconType(int type) {
        switch (type) {
            case Category.Type.FOOD:
                return R.drawable.ic_food;
            case Category.Type.ENTERTAINMENT:
                return R.drawable.ic_entertainment;
            case Category.Type.HOSPITAL:
                return R.drawable.ic_hospital;
            case Category.Type.EDUCATION:
                return R.drawable.ic_education;
            case Category.Type.TRAVEL:
                return R.drawable.ic_travel;
            case Category.Type.TRAFFIC:
                return R.drawable.ic_traffic;
            case Category.Type.SOCIAL:
                return R.drawable.ic_social;
            case Category.Type.SHOPPING:
                return R.drawable.ic_shopping;
            case Category.Type.INCOME:
                return R.drawable.ic_income;
            case Category.Type.OTHER:
                return R.drawable.ic_normal;
            default:
                return R.drawable.ic_normal;
        }
    }
}
