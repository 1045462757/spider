package com.example.utility;

import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author tiga
 */
public class BeanUtil {

    public static ConfigurableApplicationContext applicationContext;

    public static <T> T getBean(Class<T> c) {
        return applicationContext.getBean(c);
    }
}
