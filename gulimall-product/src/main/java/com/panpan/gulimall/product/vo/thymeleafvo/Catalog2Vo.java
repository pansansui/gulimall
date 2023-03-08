package com.panpan.gulimall.product.vo.thymeleafvo;

import lombok.Data;

import java.util.List;

/**
 * @author panpan
 * @create 2021-08-29 下午4:07
 */
@Data
public class Catalog2Vo {
    private String catalog1Id;
    private List<Catalog3Vo> catalog3List;
    private String id;
    private String name;
    @Data
    public static class Catalog3Vo {
        private String catalog2Id;
        private String id;
        private String name;
    }
}
