package com.example.mapper;

import com.example.domain.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * PageMapper
 *
 * @author tiga
 * @version 1.0
 * @date 2020年3月31日14:01:00
 */
@Mapper
@Component("PageMapper")
public interface PageMapper {

    /**
     * 添加一个Page
     *
     * @param record page{title, url, contentType, p_charset,content, html, parsed}
     * @return 成功：1
     */
    int insert(Page record);

    /**
     * 根据pageId查找page
     *
     * @param pageId pageId
     * @return Page
     */
    Page selectByPrimaryKey(Integer pageId);

    /**
     * 判断page唯一性
     *
     * @param url url
     * @return 0
     */
    int countByUrl(String url);

    /**
     * 修改Page是否访问
     *
     * @param record page{pageId,parsed}
     * @return 成功：1
     */
    int updateByUrl(Page record);

    /**
     * 查找所有未解析的页面
     *
     * @return List<Page>
     */
    List<Page> listAllUnparsed();
}