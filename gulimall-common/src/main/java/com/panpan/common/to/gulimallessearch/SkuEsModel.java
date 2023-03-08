package com.panpan.common.to.gulimallessearch;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author panpan
 * @create 2021-08-28 下午12:57
 */
@Data
public class SkuEsModel {
    private Long skuId;
    private Long spuId;
    private String skuTitle;
    private BigDecimal skuPrice;
    private String skuImage;
    private Long saleCount;
    private boolean hasStock;
    private Long hotScore;
    private Long  brandId;
    private Long catalogId;
    private String brandName;
    private String brandImage;
    private String catalogName;
    private List<Attrs> attrs;


    @Data
    public static class Attrs{
        private Long attrId;
        private String attrName;
        private String attrValue;
    }


    /*
  "mappings": {
    "properties": {
      "skuId": {
        "type": "long"
      },
      "spuId":{
        "type": "keyword"
      },
      "skuTitle":{
        "type": "text",
        "analyzer": "ik_smart"
      },
      "skuPrice":{
        "type": "double"
      },
      "skuImage":{
        "type": "keyword",
        "index": false,
        "doc_values": false
      },
      "saleCount":{
        "type": "long"
      },
      "hasStock":{
        "type": "boolean"
      },
      "hotScore":{
        "type": "long"
      },
      "brandId":{
        "type": "long"
      },
      "catalogId":{
        "type": "long"
      },
      "brandName":{
        "type": "keyword",
        "index": false,
        "doc_values": false
      },
      "brandImage":{
        "type": "keyword",
        "index": false,
        "doc_values": false
      },
      "catalogName":{
        "type": "keyword",
        "index": false,
        "doc_values": false
      },
      "attrs":{
       "properties": {
         "attrId":{
           "type": "long"
         },
         "attrName":{
           "type": "keyword",
           "index": false,
           "doc_values": false
         },
         "attrValue":{
           "type": "keyword"
         }
       }
      }
    }
  }
     */
}
