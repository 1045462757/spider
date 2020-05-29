package com.example.spider;

import com.example.domain.Link;
import com.example.domain.Page;
import com.example.mapper.LinkMapper;
import com.example.mapper.PageMapper;
import com.example.store.LinkStore;
import com.example.store.PageStore;
import com.example.utility.BeanUtil;
import com.example.utility.StringUtil;
import com.example.utility.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

/**
 * 爬虫主程序
 *
 * @author tiga
 * @version 1.0
 * @date 2020/4/1
 */
public class JdlySpider {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 注入LinkMapper
     */
    private LinkMapper linkMapper = BeanUtil.getBean(LinkMapper.class);

    /**
     * 注入PageMapper
     */
    private PageMapper pageMapper = BeanUtil.getBean(PageMapper.class);

    /**
     * 添加种子url
     *
     * @param seed 种子url
     */
    public void seed(String seed) {
        if (StringUtil.isEmpty(seed) || !Util.validUrl(seed)) {
            logger.info("seed无效:" + seed);
            return;
        }

        //在数据库中查找seed
        if (linkMapper.countByUrl(seed) != 0) {
            logger.info("种子url已存在");

            //从数据库中加载
            addLinksFormDB();
            addPagesFormDB();
            return;
        }

        Link link = new Link(seed);
        LinkStore.getInstance().addUnvisitedLink(link);
    }

    /**
     * 从数据库获取未访问链接
     */
    private void addLinksFormDB() {
        List<Link> links = linkMapper.listAllUnvisited();
        if (Objects.nonNull(links) && links.size() > 0) {
            LinkStore.getInstance().addUnvisitedLinks(links);
        } else {
            logger.info("link已访问完毕");
        }
    }

    /**
     * 从数据库获取未解析页面
     */
    private void addPagesFormDB() {
        List<Page> pages = pageMapper.listAllUnparsed();
        if (Objects.nonNull(pages) && pages.size() > 0) {
            PageStore.getInstance().addUnvisitedPages(pages);
        } else {
            logger.info("page已访问完毕");
        }
    }
}
