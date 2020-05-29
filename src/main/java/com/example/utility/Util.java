package com.example.utility;

import com.example.domain.Link;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author taiga
 * @date 2020/2/29
 */
public class Util {

    private static Pattern pattern = Pattern.compile("[\\s\\\\/:\\*\\?\\\"<>\\|]");

    /**
     * 消除非法字符（文件夹名称）
     *
     * @param name
     * @return
     */
    public static String removeIllegalCharacter(String name) {
        Matcher matcher = pattern.matcher(name);
        return matcher.replaceAll("");
    }

    /**
     * 获取links
     */
    public static Set<Link> getLinks(String html) {

        if (StringUtil.isEmpty(html)) {
            return null;
        }

        HashSet<Link> links = new HashSet();
        HashSet<String> urls = new HashSet<>();

        int start = 0;
        int end = 0;

        while (start != -1) {
            start = html.indexOf("https", end);
            end = html.indexOf(".html", start);

            if (start == -1 || end == -1) {
                break;
            }

            if (end - start > 60) {
                start = start + 5;
                end = start;
                continue;
            }

            String url = html.substring(start, end) + ".html";
            url = url.replace("\\", "");
            urls.add(url);
        }

        for (String url : urls) {
            links.add(new Link(url));
        }

        return links;
    }

    /**
     * 验证是否是URL
     *
     * @param url url
     * @return 是：true
     */
    public static boolean validUrl(String url) {
        if (StringUtil.isEmpty(url)) {
            return false;
        }

        url = url.toLowerCase();
        String regex = "^((https|http|ftp|rtsp|mms)?://)"
                + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?"
                + "(([0-9]{1,3}\\.){3}[0-9]{1,3}"
                + "|"
                + "([0-9a-z_!~*'()-]+\\.)*"
                + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\."
                + "[a-z]{2,6})"
                + "(:[0-9]{1,5})?"
                + "((/?)|"
                + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
        return url.matches(regex);
    }
}
