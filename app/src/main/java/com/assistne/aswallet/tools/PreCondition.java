package com.assistne.aswallet.tools;

/**
 * 常用状态判断工具类
 * Created by assistne on 16/5/27.
 */
public class PreCondition {
    /**
     * 检查是否为空, 为空抛出异常 */
    public static Object checkNotNull(Object reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }
}
