package com.panpan.gulimall.product.web;

import com.panpan.gulimall.product.service.SkuInfoService;
import com.panpan.gulimall.product.vo.thymeleafvo.SkuItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.ExecutionException;

/**
 * @author panpan
 * @create 2021-09-03 下午5:00
 */
@Controller
public class ItemController {
    @Autowired
    SkuInfoService skuInfoService;
    @GetMapping("/{skuId}.html")
    public String getItem(@PathVariable("skuId")Long skuId, Model model) throws ExecutionException, InterruptedException {
        SkuItemVo skuItemVo=skuInfoService.getSkuItemVo(skuId);
        model.addAttribute("skuItem",skuItemVo);
        return "item";
    }
}
