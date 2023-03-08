package com.panpan.gulimall.essearch.controller;

import com.panpan.gulimall.essearch.service.MallSearchService;
import com.panpan.gulimall.essearch.vo.SearchParamVo;
import com.panpan.gulimall.essearch.vo.SearchResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author panpan
 * @create 2021-09-02 上午9:42 aaa
 */
@Controller
public class EsSearchController {

    @Autowired
    MallSearchService mallSearchService;
    @GetMapping({"/list.html","/search.html"})
    public String searchAndList(SearchParamVo params, Model model){
        SearchResultVo result= mallSearchService.search(params);
        model.addAttribute("result",result);

        return "index";
    }
}
