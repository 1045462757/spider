package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpiderApplicationTests {

    @Test
    void contextLoads() {
        System.out.println("hello world");
    }

    @Test
    void test(){
        String url = "https://hylovecode/user/123";
        System.out.println(url.contains("users"));
    }
}
