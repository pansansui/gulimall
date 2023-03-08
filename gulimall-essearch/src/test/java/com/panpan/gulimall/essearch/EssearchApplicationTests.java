package com.panpan.gulimall.essearch;

import lombok.Data;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class EssearchApplicationTests {
    @Qualifier("restHighLevelClient")
    @Autowired
    RestHighLevelClient esClient;

    @Test
    void search() {

    }

    @Test
    void contextLoads() throws IOException {


    }
    @Data
    class Man{
        private String name;
        private String gender;
        private  Integer age;

        public Man(String name, String gender, Integer age) {
            this.name = name;
            this.gender = gender;
            this.age = age;
        }
    }
}
