package com.panpan.common.constant;

/**
 * @author panpan
 * @create 2021-08-28 下午8:15
 */

public class ProductConst {
    public enum SpuInfoPublishStatus{
        SPU_CREATED(0,"新建"),
        SPU_UP(1,"商品上架"),
        SPU_DOWN(2,"商品下架");
        private Integer code;
        private String msg;

        public Integer getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }

        SpuInfoPublishStatus(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }
}
