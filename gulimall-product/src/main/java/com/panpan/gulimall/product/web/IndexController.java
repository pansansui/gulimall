package com.panpan.gulimall.product.web;

import com.panpan.gulimall.product.entity.CategoryEntity;
import com.panpan.gulimall.product.service.CategoryService;
import com.panpan.gulimall.product.vo.thymeleafvo.Catalog2Vo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author panpan
 * @create 2021-08-29 下午3:13
 */
@Slf4j
@Controller
public class IndexController {
    @Autowired
    RedissonClient redissonClient;
    @Autowired
    private CategoryService categoryService;
    @GetMapping({"/","/index.html","/index"})
    public String indexPage(Model model){
        List<CategoryEntity> categoryEntities=categoryService.getCategorysForLevel1();
        model.addAttribute("categorys",categoryEntities);
        return "index";
    }
    @Cacheable(value = "product",key = "#root.methodName",sync = true)
    @ResponseBody
    @GetMapping({"/index/catalog.json"})
    public Map<String, List<Catalog2Vo>> getCatalogJson(){
        Map<String, List<Catalog2Vo>> result=categoryService.getCategorysGTLevel1();
        return result;
    }

}
