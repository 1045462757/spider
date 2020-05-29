package com.example.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 图片存储器
 *
 * @author tiga
 * @version 1.0
 * @date 2020/2/29
 */
public class ImageStore {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private ImageStore() {
    }

    private static class SingletonHolder {
        private static ImageStore imageStore = new ImageStore();
    }

    public static ImageStore getInstance() {
        return SingletonHolder.imageStore;
    }

    /**
     * 图片队列
     */
    private Queue<Map<String, List<String>>> imageMapQueue = new LinkedList<>();

    /**
     * 获取图片
     *
     * @return 图片集合
     */
    public synchronized Map<String, List<String>> getImages() {
        Map<String, List<String>> image = imageMapQueue.poll();
        logger.info("剩余图片数量:" + getImageMapSize());
        return image;
    }

    /**
     * 添加图片
     */
    public synchronized void addImages(Map<String, List<String>> image) {
        if (Objects.isNull(image)) {
            logger.info("图片为空");
            return;
        }

        imageMapQueue.add(image);
    }

    /**
     * 判断图片集合是否为空
     *
     * @return 空:true
     */
    public boolean isImageMapEmpty() {
        return imageMapQueue.isEmpty();
    }

    /**
     * 获取图片集合长度
     *
     * @return 集合长度
     */
    private int getImageMapSize() {
        return imageMapQueue.size();
    }
}
