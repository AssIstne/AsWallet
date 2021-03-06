package com.assistne.aswallet.database.dao;

import com.assistne.aswallet.database.bean.Tag;

import java.util.List;

/**
 * Created by assistne on 16/6/6.
 */
public interface TagDao {
    Tag getTag(long id);
    /**
     * @param count 取出的数量, -1表示全部*/
    List<Tag> getTagList(int count);

    List<Tag> getIncomeTagList(int count);

    List<Tag> getExpenseTagList(int count);

    List<Tag> getTagListByCatId(long catId);

    void updateTag(Tag tag);
    Tag increaseTag(long id);
}
