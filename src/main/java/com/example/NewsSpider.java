package com.example;

import com.example.spider.JdlyParser;
import com.example.spider.JdlySpider;
import com.example.spider.JdlyVisitor;
import com.example.utility.BeanUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 10454
 */
@SpringBootApplication
public class NewsSpider {

    public static void main(String[] args) {
        BeanUtil.applicationContext = SpringApplication.run(NewsSpider.class);

        JdlySpider jdlySpider = new JdlySpider();
        JdlyVisitor visitor = new JdlyVisitor();
        JdlyParser parser = new JdlyParser();

        jdlySpider.seed("https://www.jdlingyu.mobi/");

        Thread visitor1 = new Thread(visitor, "Visitor1");
        Thread visitor2 = new Thread(visitor, "Visitor2");
        Thread visitor3 = new Thread(visitor, "Visitor3");
        Thread visitor4 = new Thread(visitor, "Visitor4");
        Thread visitor5 = new Thread(visitor, "Visitor5");

        Thread parser1 = new Thread(parser, "Parser1");
        Thread parser2 = new Thread(parser, "Parser2");
        Thread parser3 = new Thread(parser, "Parser3");
        Thread parser4 = new Thread(parser, "Parser4");
        Thread parser5 = new Thread(parser, "Parser5");

        visitor1.start();
        visitor2.start();
        visitor3.start();
        visitor4.start();
        visitor5.start();

        parser1.start();
        parser2.start();
        parser3.start();
        parser4.start();
        parser5.start();
    }
}
