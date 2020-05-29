package com.example.component;

import com.example.utility.Config;
import com.example.utility.StringUtil;
import com.example.utility.Util;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * 图片下载类
 *
 * @author tiga
 * @version 1.0
 * @since 2020年2月27日16:29:22
 */
public class ImageDownload {

    private Logger logger = LoggerFactory.getLogger(getClass());

    static {
        File imageRoot = new File(Config.ROOT);
        if (!imageRoot.exists()) {
            boolean isCreate = imageRoot.mkdir();
            if (!isCreate) {
                System.out.println("图片根目录文件夹创建失败");
            }
        }
    }

    /**
     * 单张图片下载
     */
    private boolean downloadImage(String dirName, String imageUrl) {
        if (StringUtil.isEmpty(dirName) || StringUtil.isEmpty(imageUrl)) {
            logger.info("图片链接为空");
            return false;
        }

        try {
            URL url = new URL(imageUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            conn.setReadTimeout(5 * 1000);

            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            String date = ft.format(new Date());
            File img = new File(Config.ROOT + "/" + dirName, date + "-" + UUID.randomUUID().toString().substring(0, 8) + Config.SUFFIX);

            if (conn.getResponseCode() == HttpStatus.SC_OK) {
                BufferedImage read = ImageIO.read(conn.getInputStream());
                if (Objects.isNull(read)) {
                    logger.info("read为null");
                }

                return ImageIO.write(read, "JPG", img);
            } else if (conn.getResponseCode() == HttpStatus.SC_MOVED_PERMANENTLY || conn.getResponseCode() == HttpStatus.SC_MOVED_TEMPORARILY) {
                logger.info("code:" + conn.getResponseCode() + ",message:" + conn.getResponseMessage());

                String location = conn.getHeaderField("Location");
                URL locationUrl = new URL(location);
                conn = (HttpURLConnection) locationUrl.openConnection();
                conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5 * 1000);
                conn.setReadTimeout(5 * 1000);

                if (conn.getResponseCode() == HttpStatus.SC_OK) {
                    BufferedImage read = ImageIO.read(conn.getInputStream());
                    if (Objects.isNull(read)) {
                        logger.info("read为null");
                    }

                    return ImageIO.write(read, "JPG", img);
                } else {
                    logger.info("code:" + conn.getResponseCode() + ",message:" + conn.getResponseMessage());
                    return false;
                }
            } else {
                logger.info("code:" + conn.getResponseCode() + ",message:" + conn.getResponseMessage());
                return false;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    /**
     * 创建子文件夹
     *
     * @return 存在为true
     */
    private boolean createMkdir(String dirName) {
        if (StringUtil.isEmpty(dirName)) {
            return false;
        }
        dirName = Util.removeIllegalCharacter(dirName);
        File image = new File(Config.ROOT + "/" + dirName);

        if (image.exists()) {
            if (image.length() != 0) {
                logger.info("文件夹已存在且不为空,取消下载:" + Config.ROOT + "/" + dirName);
                return false;
            } else {
                logger.info("文件夹为空,删除:" + Config.ROOT + "/" + dirName);
                if (!image.delete()) {
                    logger.info("删除失败:" + Config.ROOT + "/" + dirName);
                    return false;
                }
            }
        }

        boolean isCreate = image.mkdir();
        if (!isCreate) {
            logger.info("图片文件夹创建失败:" + Config.ROOT + "/" + dirName);
            return false;
        } else {
            logger.info("图片文件夹创建成功:" + Config.ROOT + "/" + dirName);
            return true;
        }
    }

    /**
     * 图片列表下载
     *
     * @param dirName   文件夹名
     * @param imageList 图片列表
     */
    public void download(String dirName, ArrayList<String> imageList) {
        if (StringUtil.isEmpty(dirName) || Objects.isNull(imageList) || imageList.size() == 0) {
            logger.info("图片为空或无效的名称:" + dirName);
            return;
        }

        if (!createMkdir(dirName)) {
            return;
        }

        int num = 1;

        for (String image : imageList) {
            if (downloadImage(dirName, image)) {
                logger.info("(" + num + "/" + imageList.size() + ")" + "下载图片成功");
            } else {
                logger.info("(" + num + "/" + imageList.size() + ")" + "下载图片失败");
            }
            num++;
        }
    }
}
