package com.papan.gulimall.auth_server.controller;

import com.panpan.common.utils.R;
import com.papan.gulimall.auth_server.fegin.MemberFeign;
import com.papan.gulimall.auth_server.vo.LoggingVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;

/**
 * @author panpan
 * @create 2021-09-08 上午11:42
 */
@Controller
public class LoggingController {
    @Autowired
    MemberFeign memberFeign;

    @PostMapping("logging")
    public String logging(LoggingVo loggingVo, RedirectAttributes redirectAttributes){
        R logging = memberFeign.logging(loggingVo);
        if (logging.getCode()==0){
            return "redirect:http://gulimall.com";
        }
        HashMap<String, String> errors = new HashMap<>();
        errors.put("msg", (String) logging.get("msg"));
        redirectAttributes.addFlashAttribute("errors",errors);
        return "redirect:http://auth.gulimall.com ";
    }



}
