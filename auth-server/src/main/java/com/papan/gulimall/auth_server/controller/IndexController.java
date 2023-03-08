package com.papan.gulimall.auth_server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author panpan
 * @create 2021-09-06 下午7:13
 */
@Controller
public class IndexController {

    @RequestMapping({"/","index.html","index","login","login.html"})
    public String loginPage(){
        return "index";
    }
    @RequestMapping({"/register","/register.html"})
    public String registerPage(){
        return "register";
    }
}
