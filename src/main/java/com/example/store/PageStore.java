package com.example.store;

import com.example.domain.Page;
import com.example.mapper.PageMapper;
import com.example.utility.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 页面存储器
 *
 * @author tiga
 * @version 1.0 -> 1.1
 * @date 2020/2/29 -> 2020/4/5
 */
public class PageStore {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private PageStore() {
    }

    private static class SingletonHolder {
        private static PageStore pageStore = new PageStore();
    }

    public static PageStore getInstance() {
        return SingletonHolder.pageStore;
    }

    /**
     * 已解析界面队列
     */
    private Queue<Page> parsedPages = new LinkedList<>();

    /**
     * 未解析页面队列
     */
    private Queue<Page> unParsedPages = new LinkedList<>();

    /**
     * 注入PageMapper
     */
    private PageMapper pageMapper = BeanUtil.getBean(PageMapper.class);

    /**
     * 取出未访问页面队列的首个链接,并从队列中删除该页面
     *
     * @return 未访问页面队列的首个页面
     */
    public synchronized Page getFirst() {
        return unParsedPages.poll();
    }

    /**
     * 添加已解析页面
     *
     * @param page 已解析页面
     */
    public synchronized void addParsedPages(Page page) {
        if (Objects.isNull(page) || Objects.isNull(page.getTitle()) || Objects.isNull(page.getUrl())) {
            logger.info("页面为空");
            return;
        }

        page.setParsed(true);
        parsedPages.add(page);
        if (pageMapper.updateByUrl(page) != 1) {
            logger.warn("更新访问信息失败:" + page.getUrl());
        }

        logger.info("已解析页面数量:" + getParsedSize() + ",已解析一个页面:title:" + page.getTitle() + ",url:" + page.getUrl());
    }

    /**
     * 添加未解析页面
     *
     * @param page 未解析页面
     */
    public synchronized void addUnVisitedPage(Page page) {
        if (Objects.isNull(page) || Objects.isNull(page.getUrl())) {
            logger.info("页面为空");
            return;
        }

        if (!parsable(page)) {
            logger.info("该页面已在解析队列中:" + page.getUrl());
            return;
        }

        if (pageMapper.countByUrl(page.getUrl()) != 0) {
            logger.info("数据库中已存在此url:" + page.getUrl());
            return;
        }

        unParsedPages.add(page);

        if (pageMapper.insert(page) != 1) {
            logger.warn("添加至数据库失败:" + page.getUrl());
        }

        logger.info("待解析页面数量:" + getUnParsedSize() + ",添加一个未解析页面:url:" + page.getUrl());
    }

    /**
     * 添加为解析页面集合
     *
     * @param pageList 未解析页面集合
     */
    public void addUnvisitedPages(List<Page> pageList) {
        if (Objects.isNull(pageList) || pageList.size() == 0) {
            logger.info("pageList为空");
            return;
        }

        unParsedPages.addAll(pageList);
    }

    /**
     * 判断未解析页面队列是否为空
     *
     * @return true 空
     */
    public boolean isUnParsedEmpty() {
        return unParsedPages.isEmpty();
    }

    /**
     * 获取已解析页面的长度
     *
     * @return 已解析页面的长度
     */
    private int getParsedSize() {
        return parsedPages.size();
    }

    /**
     * 获取已解析页面
     *
     * @param num 页面数
     * @return 已解析页面集合
     */
    public synchronized List<Page> getParsed(int num) {
        if (num <= 0) {
            return null;
        }

        if (parsedPages.size() >= num) {
            List<Page> pages = new ArrayList<>();
            for (int i = 0; i < num; i++) {
                pages.add(parsedPages.peek());
            }
            return pages;
        }

        return null;
    }

    /**
     * 判断该页面是否在未解析页面队列中
     *
     * @param page 要判断的页面
     * @return true 已在
     */
    private boolean parsable(Page page) {
        if (Objects.isNull(page) || Objects.isNull(page.getUrl())) {
            return false;
        }

        for (Page unParsedPage : unParsedPages) {
            if (Objects.equals(unParsedPage.getUrl(), page.getUrl())) {
                return false;
            }
        }

        for (Page parsedPage : parsedPages) {
            if (Objects.equals(parsedPage.getUrl(), page.getUrl())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 返回未解析页面的长度
     *
     * @return 未访问解析的长度
     */
    private int getUnParsedSize() {
        return unParsedPages.size();
    }
}
