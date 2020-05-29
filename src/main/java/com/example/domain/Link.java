package com.example.domain;

import com.example.utility.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Objects;

/**
 * Link类
 *
 * @author tiga
 * @version 1.0
 * @date 2020年3月24日12:03:37
 */
public class Link {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * linkId
     */
    private Integer linkId;

    /**
     * 数据生成时间
     */
    private Date createTime;

    /**
     * 最后修改时间
     */
    private Date updateTime;

    /**
     * url
     */
    private String url;

    /**
     * 是否访问
     */
    private Boolean visited;

    public Link() {
    }

    public Link(String url) {
        setUrl(url);
        this.visited = false;
    }

    public Integer getLinkId() {
        return linkId;
    }

    public void setLinkId(Integer linkId) {
        this.linkId = linkId;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        if (Util.validUrl(url)) {
            this.url = url;
        } else {
            logger.warn("不是有效的url:" + url);
        }
    }

    public Boolean getVisited() {
        return visited;
    }

    public void setVisited(Boolean visited) {
        this.visited = visited;
    }

    @Override
    public String toString() {
        return "Link{" +
                "linkId=" + linkId +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", url='" + url + '\'' +
                ", visited=" + visited +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Link link = (Link) o;
        return Objects.equals(linkId, link.linkId) &&
                Objects.equals(createTime, link.createTime) &&
                Objects.equals(updateTime, link.updateTime) &&
                Objects.equals(url, link.url) &&
                Objects.equals(visited, link.visited);
    }

    @Override
    public int hashCode() {
        return Objects.hash(linkId, createTime, updateTime, url, visited);
    }
}