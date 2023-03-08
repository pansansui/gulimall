package com.panpan.gulimall.essearch.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author panpan
 * @create 2021-09-01 下午8:07
 */
@Controller
public class IndexController {

    @GetMapping({"/","/index.html","/index"})
    public String indexPage(){

        return "index";
    }

}
