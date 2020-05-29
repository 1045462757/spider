package com.example.utility;

/**
 * 字符串工具类
 *
 * @author tiga
 * @version 1.0
 * @date 2020/2/29
 */
public class StringUtil {

    /**
     * 判断是否为空字符串
     *
     * @param str 待测字符串
     * @return 如果为空，则返回true
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 判断字符串是否非空
     *
     * @param str 待测字符串
     * @return 如果不为空，则返回true
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
}
