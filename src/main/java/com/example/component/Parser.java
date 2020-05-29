package com.example.component;

import com.example.domain.Link;
import com.example.domain.Page;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 页面解析类
 *
 * @author tiga
 * @version 1.0
 * @since 2020/2/27
 */
public class Parser {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 解析指定Page获取指定CSS模块包含的Link对象集合
     * <p>
     *
     * @param page        指定的Page对象
     * @param attribute   要获取的数据
     * @param cssSelector 指定的CSS选择器
     * @return Set 当前page中指定CSS选择器包含的Link对象集合
     * @author tiga
     */
    public Set<Link> getLinks(Page page, String attribute, String... cssSelector) {
        if (Objects.isNull(page) || Objects.isNull(attribute) || attribute.length() == 0 || Objects.isNull(cssSelector) || cssSelector.length == 0) {
            logger.info("参数无效,无法解析页面中的Link");
            return null;
        }

        Elements elements = null;
        for (int i = 0; i < cssSelector.length; i++) {
            if (i == 0) {
                elements = page.getDocument().select(cssSelector[i]);
            } else {
                elements = elements.select(cssSelector[i]);
            }
        }

        Set<Link> links = new HashSet<>();
        for (Element e : elements) {
            links.add(new Link(e.attr(attribute)));
        }
        return links;
    }

    /**
     * 解析指定Page获取CSS模块包含的Image对象集合
     *
     * @param page        指定的Page对象
     * @param attribute   要获取的数据
     * @param cssSelector 指定的CSS选择器
     * @return ArrayList<Image> 当前page中指定CSS选择器包含的Image对象集合
     */
    public ArrayList<String> getImage(Page page, String attribute, String... cssSelector) {
        if (Objects.isNull(page) || Objects.isNull(attribute) || attribute.length() == 0 || Objects.isNull(cssSelector) || cssSelector.length == 0) {
            logger.info("参数无效,无法解析页面中的Image");
            return null;
        }

        Elements elements = null;
        for (int i = 0; i < cssSelector.length; i++) {
            if (i == 0) {
                elements = page.getDocument().select(cssSelector[i]);
            } else {
                elements = elements.select(cssSelector[i]);
            }
        }

        ArrayList<String> images = new ArrayList<>();
        for (Element e : elements) {
            images.add(e.attr(attribute));
        }
        return images;
    }
}
