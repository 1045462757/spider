package com.example.component;

import com.alibaba.fastjson.JSON;
import com.example.domain.Page;
import com.example.utility.Util;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.*;

/**
 * url访问类
 *
 * @author tiga
 * @version 1.0
 * @since 2020/2/27
 */
public class Visitor {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 访问配置
     */
    private RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(5 * 1000)
            .setSocketTimeout(5 * 1000)
            .setConnectionRequestTimeout(5 * 1000)
            .build();

    /**
     * 客户端配置
     */
    private CloseableHttpClient closeableHttpClient = HttpClients.custom()
            .setDefaultRequestConfig(requestConfig)
            .setRetryHandler(new DefaultHttpRequestRetryHandler())
            .setUserAgent("Mozilla/5.0(Windows;U;Windows NT 5.1;en-US;rv:0.9.4)")
            .build();

    /**
     * 判断url是否合法
     *
     * @return 不合法:true
     */
    private boolean isUnValidUrl(String url) {
        if (!Util.validUrl(url)) {
            logger.info("url无效:" + url);
            return true;
        }
        return false;
    }

    /**
     * 执行请求获得页面
     *
     * @param httpMethod 请求
     * @return Page
     */
    private Page visit(HttpRequestBase httpMethod, String url) {
        CloseableHttpResponse response = null;
        HttpEntity httpEntity = null;
        try {
            if (Objects.isNull(httpMethod)) {
                throw new Exception("httpMethod为空");
            }

            response = closeableHttpClient.execute(httpMethod);

            if (Objects.isNull(response)) {
                throw new Exception("response为空");
            }

            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw new Exception("访问" + url + "失败," + "代码:" + response.getStatusLine().getStatusCode() + ",原因:" + response.getStatusLine().getReasonPhrase());
            }

            httpEntity = response.getEntity();

            if (Objects.isNull(httpEntity)) {
                throw new Exception("httpEntity为空");
            }

            byte[] responseBody = EntityUtils.toByteArray(httpEntity);

            String contentType = null;

            if (Objects.nonNull(httpEntity.getContent())) {
                contentType = httpEntity.getContentType().getValue();
            }

            return new Page(responseBody, url, contentType);
        } catch (Exception e) {
            logger.error("异常信息:" + e.getMessage());
            return null;
        } finally {
            try {
                if (Objects.nonNull(httpEntity)) {
                    EntityUtils.consume(httpEntity);
                }
                if (Objects.nonNull(response)) {
                    response.close();
                }
            } catch (IOException e) {
                logger.error("异常信息:" + e.getMessage());
            }
        }
    }

    /**
     * get方式 无参
     *
     * @param url 链接
     * @return Page
     */
    public Page visitForGet(String url) {
        return isUnValidUrl(url) ? null : visit(new HttpGet(url), url);
    }

    /**
     * get方式 有参
     *
     * @param url    链接
     * @param params 参数
     * @return Page
     */
    public Page visitForGet(String url, Map<String, String> params) {
        if (isUnValidUrl(url) || Objects.isNull(params)) {
            logger.info("params为空");
            return null;
        }

        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue());
            }
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            return visit(httpGet, url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * post方式 无参
     *
     * @param url 链接
     * @return Page
     */
    public Page visitForPost(String url) {
        if (isUnValidUrl(url)) {
            return null;
        }

        HttpPost httpPost = new HttpPost(url);
        return visit(httpPost, url);
    }

    /**
     * post方式 带参数 application/x-www-form-urlencoded
     *
     * @param url    链接
     * @param params 参数
     * @return Page
     */
    public Page visitForPost(String url, Map<String, String> params) {
        if (isUnValidUrl(url)) {
            return null;
        }

        if (Objects.isNull(params)) {
            return null;
        }

        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

        List<NameValuePair> nameValuePairs = new ArrayList<>();
        Set<Map.Entry<String, String>> entrySet = params.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return visit(httpPost, url);
    }

    /**
     * post方式 带参数 application/json
     *
     * @param url    链接
     * @param params 参数
     * @return Page
     */
    public Page visitForPostJson(String url, Map<String, String> params) {
        if (isUnValidUrl(url)) {
            return null;
        }

        if (Objects.isNull(params)) {
            return null;
        }

        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json");

        StringEntity stringEntity = new StringEntity(JSON.toJSONString(params), "UTF-8");
        httpPost.setEntity(stringEntity);

        return visit(httpPost, url);
    }

    /**
     * 关闭客户端
     */
    public void close() {
        try {
            logger.info("关闭客户端");
            closeableHttpClient.close();
        } catch (IOException e) {
            logger.warn("异常信息:" + e.getMessage());
        }
    }
}
