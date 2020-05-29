//package com.example;
//
//import com.example.domain.Link;
//import com.example.domain.Page;
//import com.example.spider.JdlyImageDownload;
//import com.example.spider.JdlyParser;
//import com.example.spider.JdlyVisitor;
//import com.example.store.LinkStore;
//import com.example.store.PageStore;
//import com.example.utility.BeanUtil;
//import com.example.utility.Util;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * 爬虫主程序
// *
// * @author tiga
// * @version 1.0
// * @since 2020/2/27
// */
//@SpringBootApplication
//public class Spider {
//
//    public static void main(String[] args) {
//        BeanUtil.applicationContext = SpringApplication.run(Spider.class);
//
//        JdlyVisitor visitor = new JdlyVisitor();
//        JdlyParser parser = new JdlyParser();
//        JdlyImageDownload imageDownload = new JdlyImageDownload();
//
//        for (int i = 1; i <= 6; i++) {
//
//            System.out.println("========================================");
//            System.out.println("===============第" + i + "页=============");
//            System.out.println("========================================");
//
//            // 添加种子url
//            LinkStore.getInstance().seed("https://www.jdlingyu.mobi/wp-admin/admin-ajax.php?action=zrz_load_more_posts");
//
//            Map<String, String> params = new HashMap<>();
//            params.put("type", "tag3034");
//            params.put("paged", String.valueOf(i));
//
//            PageStore.getInstance().addUnVisitedPage(visitor.visitForPost(LinkStore.getInstance().getFirst().getUrl(), params));
//
//            for (Link link : Util.getLinks(PageStore.getInstance().getFirst().getHtml())) {
//                LinkStore.getInstance().addUnvisitedLink(link);
//            }
//
//            while (!LinkStore.getInstance().isUnvisitedEmpty()) {
//                Page page = visitor.visitForGet(LinkStore.getInstance().getFirst().getUrl());
//                PageStore.getInstance().addUnVisitedPage(page);
//            }
//
//            while (!PageStore.getInstance().isUnParsedEmpty()) {
//                Page page = PageStore.getInstance().getFirst();
//                ArrayList<String> arrayList = parser.getImage(page, "src", "div[id=content-innerText]", "img");
//                imageDownload.download(Util.removeIllegalCharacter(page.getTitle()), arrayList);
//            }
//        }
//
//        visitor.close();
//    }
//}
