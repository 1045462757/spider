package com.example.mapper;

import com.example.domain.Link;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * LinkMapper
 *
 * @author tiga
 * @version 1.0
 * @date 2020年3月31日13:35:07
 */
@Mapper
@Component("LinkMapper")
public interface LinkMapper {

    /**
     * 添加一个Link
     *
     * @param record link{url,visited}
     * @return 成功：1
     */
    int insert(Link record);

    /**
     * 根据LinkId查找Link
     *
     * @param linkId linkId
     * @return Link
     */
    Link selectByPrimaryKey(Integer linkId);

    /**
     * 判断url唯一性
     *
     * @param url url
     * @return 0
     */
    int countByUrl(String url);

    /**
     * 修改Link是否访问
     *
     * @param record link{linkId,visited}
     * @return 成功：1
     */
    int updateByUrl(Link record);

    /**
     * 查找所有未访问的链接
     *
     * @return List<Link>
     */
    List<Link> listAllUnvisited();
}