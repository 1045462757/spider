package com.example.store;

import com.example.domain.Link;
import com.example.mapper.LinkMapper;
import com.example.utility.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 链接存储器
 *
 * @author tiga
 * @version 1.0 -> 1.1
 * @date 2020/2/29 -> 2020/4/5
 */
public class LinkStore {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private LinkStore() {
    }

    private static class SingletonHolder {
        private static LinkStore linkStore = new LinkStore();
    }

    public static LinkStore getInstance() {
        return SingletonHolder.linkStore;
    }

    /**
     * 已访问链接集合
     */
    private Set<Link> visitedLinks = new HashSet<>();

    /**
     * 未访问链接队列
     */
    private Queue<Link> unVisitedLinks = new LinkedList<>();

    /**
     * 注入LinkMapper
     */
    private LinkMapper linkMapper = BeanUtil.getBean(LinkMapper.class);

    /**
     * 取出未访问链接队列的首个链接,并从队列中删除该链接,并加入已访问链接集合
     *
     * @return 未访问链接队列的首个链接
     */
    public synchronized Link getFirst() {
        return unVisitedLinks.poll();
    }

    /**
     * 添加未访问链接
     *
     * @param link 未访问链接
     */
    public synchronized void addUnvisitedLink(Link link) {
        if (Objects.isNull(link) || Objects.isNull(link.getUrl())) {
            logger.info("url为空");
            return;
        }

        if (!visitable(link)) {
            logger.info("该链接已访问或待访问:" + link.getUrl());
            return;
        }

        if (linkMapper.countByUrl(link.getUrl()) != 0) {
            logger.info("数据库中已存在此url:" + link.getUrl());
            return;
        }

        unVisitedLinks.add(link);

        if (linkMapper.insert(link) != 1) {
            logger.warn("添加至数据库失败:" + link.getUrl());
        }

        logger.info("待访问链接数量:" + getUnvisitedSize() + ",添加一个未访问链接:" + link.getUrl());
    }

    /**
     * 添加未访问链接集合
     *
     * @param linkList 未访问链接集合
     */
    public synchronized void addUnvisitedLinks(List<Link> linkList) {
        if (Objects.isNull(linkList) || linkList.size() == 0) {
            logger.info("linkList为空");
            return;
        }

        unVisitedLinks.addAll(linkList);
    }

    /**
     * 判断未访问链接队列是否为空
     *
     * @return true 空
     */
    public boolean isUnvisitedEmpty() {
        return unVisitedLinks.isEmpty();
    }

    /**
     * 返回未访问链接队列的长度
     *
     * @return 未访问链接的数量
     */
    private int getUnvisitedSize() {
        return unVisitedLinks.size();
    }

    /**
     * 返回已访问链接集合的长度
     *
     * @return 已访问链接的数量
     */
    private int getVisitedSize() {
        return visitedLinks.size();
    }

    /**
     * 添加已访问链接
     *
     * @param link 已访问链接
     */
    public synchronized void addVisitedLink(Link link) {
        if (Objects.isNull(link) || Objects.isNull(link.getUrl())) {
            logger.info("url为空");
            return;
        }

        link.setVisited(true);
        visitedLinks.add(link);
        if (linkMapper.updateByUrl(link) != 1) {
            logger.warn("更新访问信息失败:" + link.getUrl());
        }

        logger.info("已访问链接数量:" + getVisitedSize() + ",已访问一个链接:" + link.getUrl());
    }

    /**
     * 判断该链接是否已访问过或在未访问链接队列内
     *
     * @param link 要判断的链接
     * @return false 已访问
     */
    private boolean visitable(Link link) {
        if (Objects.isNull(link) || Objects.isNull(link.getUrl())) {
            return false;
        }

        for (Link visitedLink : visitedLinks) {
            if (Objects.equals(visitedLink.getUrl(), link.getUrl())) {
                return false;
            }
        }

        for (Link unVisitedLink : unVisitedLinks) {
            if (Objects.equals(unVisitedLink.getUrl(), link.getUrl())) {
                return false;
            }
        }
        return true;
    }
}
