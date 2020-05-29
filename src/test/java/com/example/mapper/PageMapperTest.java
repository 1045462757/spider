package com.example.mapper;

import com.example.domain.Page;
import com.example.component.Visitor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class PageMapperTest {

    @Resource
    private PageMapper pageMapper;

    @Test
    public void insert() {

        Visitor visitor = new Visitor();

        Page page = visitor.visitForGet("https://hylovecode.cn");

        page.setParsed(false);

        System.out.println(pageMapper.insert(page));
    }

    @Test
    public void update() {
        Page page = new Page();
        page.setUrl("https://www.jdlingyu.mobi");
        page.setParsed(true);

        System.out.println(pageMapper.updateByUrl(page));
    }

    @Test
    public void select() {
        System.out.println(pageMapper.selectByPrimaryKey(1));
    }

    @Test
    public void listAllUnparsed(){
        System.out.println(pageMapper.listAllUnparsed());
    }
}
