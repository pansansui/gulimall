package com.panpan.gulimall.essearch.service.impl;

import com.alibaba.fastjson.JSON;
import com.panpan.common.to.gulimallessearch.SkuEsModel;
import com.panpan.gulimall.essearch.config.ElasticSearchClientConfig;
import com.panpan.gulimall.essearch.constant.EsConstant;
import com.panpan.gulimall.essearch.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author panpan
 * @create 2021-08-28 下午5:39
 */
@Slf4j
@Service("productSaveService")
public class ProductSaveServiceImpl implements ProductSaveService {
    @Qualifier("restHighLevelClient")
    @Autowired
    RestHighLevelClient esClient;
    /*
    * @Description:
    * @ParamType:[java.util.List<com.panpan.common.to.gulimallessearch.SkuEsModel>]
    * @Param: skuEsModels
    * @return: Boolean 成功true 失败false
    * @Author: panpan
    * @Date:28/8/2021
    */
    @Override
    public Boolean putProductSku (List<SkuEsModel> skuEsModels)  {
        BulkRequest bulkRequest = new BulkRequest();
        skuEsModels.stream().forEach((item) -> {
            IndexRequest indexRequest = new IndexRequest(EsConstant.PRODUCT_INDEX);
            indexRequest
                    .id(item.getSkuId().toString())
                    .source(JSON.toJSONString(item), XContentType.JSON);
            bulkRequest.add(indexRequest);
        });
        BulkResponse bulkResponse = null;
        try {
            bulkResponse = esClient.bulk(bulkRequest, ElasticSearchClientConfig.getCommonOptions());
        } catch (IOException e) {
            return false;
        }
        return !bulkResponse.hasFailures();
    }
}
