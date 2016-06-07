package com.assistne.aswallet.component;

/**
 * Created by assistne on 16/6/7.
 */
public interface BackPressHandler {
    /**
     * 处理返回键时间
     * @return true表示上级不再处理该返回键 */
    boolean onBackPressed();
}
