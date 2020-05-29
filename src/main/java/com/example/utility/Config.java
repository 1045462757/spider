package com.example.utility;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 配置类
 *
 * @author tiga
 * @version 1.0
 * @since 2020年2月27日16:37:14
 */
@Component
@PropertySource("classpath:config.properties")
public class Config {

    /**
     * 项目根目录
     */
    public static String ROOT;

    /**
     * 图片后缀
     */
    public static String SUFFIX;

    @Value("${config.root}")
    public void setRoot(String root) {
        ROOT = root;
    }

    @Value("${config.suffix}")
    public void setSuffix(String suffix) {
        SUFFIX = suffix;
    }

    /**
     * 结束标志
     */
    public static boolean END = false;

    public static Lock lock = new ReentrantLock();

    public static Condition visitor = lock.newCondition();

    public static Condition parser = lock.newCondition();

    public static Condition end = lock.newCondition();
}
