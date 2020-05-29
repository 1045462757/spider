package com.example.mapper;

import com.example.domain.Link;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class LinkMapperTest {

    @Resource
    private LinkMapper linkMapper;

    @Test
    public void insert() {
        System.out.println(linkMapper.insert(new Link("http://hylovecode.cn")));
    }

    @Test
    public void select() {
        System.out.println(linkMapper.selectByPrimaryKey(1));
    }

    @Test
    public void update() {
        Link link = new Link("https://hylovecode.cn/login");
        link.setVisited(true);
        System.out.println(linkMapper.updateByUrl(link));
    }

    @Test
    public void countByUrl(){
        System.out.println(linkMapper.countByUrl("http://hylovecode.cn/login"));
    }

    @Test
    public void listAllUnvisited(){
        System.out.println(linkMapper.listAllUnvisited());
    }
}
