package com.papan.gulimall.auth_server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author panpan
 * @create 2021-09-20 下午5:41
 */

public class Test {
    @org.junit.jupiter.api.Test
    public void test1() throws IOException {
        File file = new File("E:\\尚硅谷java\\gulimall\\.git\\index");

        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader gbk = new InputStreamReader(fileInputStream, "utf8");
        char[] chars = new char[1024];
        int i;
        while ((i=gbk.read(chars))>0){
            System.out.println(chars);
        }
    }
}
