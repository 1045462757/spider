package com.example.domain;

import com.example.utility.CharsetDetector;
import com.example.utility.Util;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * 页面类
 *
 * @author tiga
 * @version 1.0
 * @date 2020/2/29
 */
public class Page {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 页面Id
     */
    private Integer pageId;

    /**
     * 数据创建时间
     */
    private Date createTime;

    /**
     * 数据更新时间
     */
    private Date updateTime;

    /**
     * 页面标题
     */
    private String title;

    /**
     * 页面url
     */
    private String url;

    /**
     * 页面内容字节数组
     */
    private byte[] content;

    /**
     * 页面文档
     */
    private Document document;

    /**
     * 页面html
     */
    private String html;

    /**
     * 页面类型
     */
    private String contentType;

    /**
     * 页面编码
     */
    private String charset;

    /**
     * 是否解析
     */
    private Boolean parsed;

    public Page() {
    }

    public Page(byte[] content, String url, String contentType) {
        setUrl(url);
        this.content = content;
        this.contentType = contentType;
        this.parsed = false;
    }

    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getTitle() {
        if (title == null) {
            Elements elements = getDocument().select("title");
            for (Element element : elements) {
                title = element.text();
            }
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        if (Util.validUrl(url)) {
            this.url = url;
        } else {
            logger.info("不是有效的url:" + url);
        }
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public Document getDocument() {
        if (document != null) {
            return document;
        }
        try {
            this.document = Jsoup.parse(getHtml(), url);
            return document;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getHtml() {
        if (html != null) {
            return html;
        }
        if (content == null) {
            return null;
        }
        if (charset == null) {
            charset = CharsetDetector.guessEncoding(content);
        }
        try {
            this.html = new String(content, charset);
            return html;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public Boolean getParsed() {
        return parsed;
    }

    public void setParsed(Boolean parsed) {
        this.parsed = parsed;
    }

    @Override
    public String toString() {
        return "Page{" +
                "pageId=" + pageId +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
//                ", content=" + Arrays.toString(content) +
//                ", document=" + document +
//                ", html='" + html + '\'' +
                ", contentType='" + contentType + '\'' +
                ", charset='" + charset + '\'' +
                ", parsed=" + parsed +
                '}';
    }
}
