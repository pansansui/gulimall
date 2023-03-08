package com.panpan.gulimall.essearch.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.panpan.common.to.gulimallessearch.SkuEsModel;
import com.panpan.gulimall.essearch.config.ElasticSearchClientConfig;
import com.panpan.gulimall.essearch.constant.EsConstant;
import com.panpan.gulimall.essearch.service.MallSearchService;
import com.panpan.gulimall.essearch.vo.SearchParamVo;
import com.panpan.gulimall.essearch.vo.SearchResultVo;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author panpan
 * @create 2021-09-01 下午9:34
 */
@Service
public class MallSearchServiceImpl implements MallSearchService {
    @Qualifier("restHighLevelClient")
    @Autowired
    RestHighLevelClient esClient;

    @Override
    public SearchResultVo search(SearchParamVo params) {
        SearchRequest searchRequest=buildRequset(params);
        SearchResponse response=null;
        try {
            response = esClient.search(searchRequest, ElasticSearchClientConfig.getCommonOptions());
        } catch (IOException e) {
            e.printStackTrace();
        }
        SearchResultVo resultVo=buildResult(response,params.getPageNum());
        return resultVo;
    }
    /*
    * @Description: 构建请求,模糊匹配，过滤（属性，分类，品牌，价格区间，库存），排序，分页，高亮，聚合分析
    * @ParamType:[com.panpan.gulimall.essearch.vo.SearchParamVo]
    * @Param: params
    * @return: SearchRequest
    * @Author: panpan
    * @Date:2/9/2021
    */

    private SearchRequest buildRequset(SearchParamVo params) {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        // 过滤（属性，分类，品牌，价格区间，库存）
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        // 关键词
        if (!StringUtils.isEmpty(params.getKeyword())){
            boolQuery.must(QueryBuilders.matchQuery("skuTitle",params.getKeyword()));
            //高亮
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuTitle");
            highlightBuilder.preTags(EsConstant.HIGHLIGHT_PRE_TAGS);
            highlightBuilder.postTags(EsConstant.HIGHLIGHT_POST_TAGS);
            builder.highlighter(highlightBuilder);
        }
        // 分类
        if(params.getCatalog3Id()!=null){
            boolQuery.filter(QueryBuilders.termQuery("catalogId",params.getCatalog3Id()));
        }
        // 品牌
        if(params.getBrandIds()!=null&&params.getBrandIds().size()>0){
            boolQuery.filter(QueryBuilders.termsQuery("brandId",params.getBrandIds()));
        }
        // 库存
        boolQuery.filter(QueryBuilders.termQuery("hasStock",params.getHasStock()==0?false:true));
        // 价格区间 0_10000;
        if (!StringUtils.isEmpty(params.getSkuPrice())&&!"_".equals(params.getSkuPrice())){
            RangeQueryBuilder skuPrice = QueryBuilders.rangeQuery("skuPrice");
            String paramsSkuPrice =params.getSkuPrice();
            String[] strings = paramsSkuPrice.split("_");
            if (paramsSkuPrice.startsWith("_")){
                skuPrice.lte(strings[1]);
            }else if (paramsSkuPrice.endsWith("_")){
                skuPrice.gte(strings[0]);
            }else if(strings.length==2){
                skuPrice.gte(strings[0]).lte(strings[1]);
            }
            boolQuery.filter(skuPrice);

        }
        // 格式 1_5寸:8寸
        if (params.getAttrs()!=null&&params.getAttrs().size()>0){
            for (String attr : params.getAttrs()) {
                String[] s = attr.split("_");
                String attrId=s[0];
                String[] attrValue = s[1].split(":");
                BoolQueryBuilder nestedBoolQuery = QueryBuilders.boolQuery();
                nestedBoolQuery.must(QueryBuilders.termQuery("attrs.attrId",attrId));
                nestedBoolQuery.must(QueryBuilders.termsQuery("attrs.attrValue",attrValue));
                boolQuery.filter(QueryBuilders.nestedQuery(EsConstant.PRODUCT_NESTED,nestedBoolQuery, ScoreMode.None));
            }
        }

        //排序，分页，高亮
        if (!StringUtils.isEmpty(params.getSort())){
            String[] s = params.getSort().split("_");
            builder.sort(s[0],"asc".equalsIgnoreCase(s[1])? SortOrder.ASC:SortOrder.DESC);
        }
        if(!StringUtils.isEmpty(params.getPageNum())){
            builder.from(params.getPageNum());
            builder.size(EsConstant.PAGE_SIZE);
        }
        //聚合分析
        //品牌聚合
        TermsAggregationBuilder brand_agg = AggregationBuilders
                .terms("brand_agg")
                .field("brandId")
                .size(10);
        TermsAggregationBuilder brandName_agg = AggregationBuilders
                .terms("brandName_agg")
                .field("brandName")
                .size(10);
        brand_agg.subAggregation(brandName_agg);
        TermsAggregationBuilder brandImage_agg = AggregationBuilders
                .terms("brandImage_agg")
                .field("brandImage")
                .size(10);
        brand_agg.subAggregation(brandImage_agg);

        //分类聚合
        TermsAggregationBuilder catalogId_agg = AggregationBuilders
                .terms("catalogId_agg")
                .field("catalogId")
                .size(10);
        TermsAggregationBuilder catalogName_agg = AggregationBuilders
                .terms("catalogName_agg")
                .field("catalogName")
                .size(10);
        catalogId_agg.subAggregation(catalogName_agg);
        //属性聚合
        NestedAggregationBuilder attrs_agg = AggregationBuilders.nested("attrs_agg", "attrs");
        TermsAggregationBuilder attrId_agg = AggregationBuilders
                .terms("attrId_agg")
                .field("attrs.attrId")
                .size(10);
        TermsAggregationBuilder attrName_agg = AggregationBuilders
                .terms("attrName_agg")
                .field("attrs.attrName")
                .size(10);
        TermsAggregationBuilder attrValue_agg = AggregationBuilders
                .terms("attrValue_agg")
                .field("attrs.attrValue")
                .size(10);
        attrId_agg.subAggregation(attrName_agg);
        attrId_agg.subAggregation(attrValue_agg);
        attrs_agg.subAggregation(attrId_agg);


        builder.aggregation(brand_agg);
        builder.aggregation(catalogId_agg);
        builder.aggregation(attrs_agg);
        builder.query(boolQuery);
        SearchRequest searchRequest = new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX}, builder);

        return searchRequest;
    }
    /*
    * @Description: 封装结果，response可传null
    * @ParamType:[org.elasticsearch.action.search.SearchResponse]
    * @Param: params
    * @return: SearchResultVo
    * @Author: panpan
    * @Date:2/9/2021
    */
    private SearchResultVo buildResult(SearchResponse response,Integer pageNum) {
        SearchResultVo resultVo = new SearchResultVo();
        long total = response.getHits().getTotalHits().value;
        Integer totalPage=(int) (total+EsConstant.PAGE_SIZE-1)/EsConstant.PAGE_SIZE;
        resultVo.setPageNum(pageNum);
        resultVo.setTotal(total);
        resultVo.setTotalPage(totalPage);

        SearchHit[] hits = response.getHits().getHits();
        if(hits!=null&&hits.length>0){
            List<SkuEsModel> products = Arrays.stream(hits).map((item) -> {
                String sourceAsString = item.getSourceAsString();
                SkuEsModel skuEsModel = JSONObject.parseObject(sourceAsString, SkuEsModel.class);
                if (item.getHighlightFields()!=null&&item.getHighlightFields().size()>0){
                    skuEsModel.setSkuTitle(item.getHighlightFields().get("skuTitle").fragments()[0].string());
                }
                return skuEsModel;
            }).collect(Collectors.toList());
            resultVo.setProducts(products);
        }

        ParsedLongTerms catalogId_agg = response.getAggregations().get("catalogId_agg");
        List<SearchResultVo.catalogVo> catalogs = catalogId_agg.getBuckets().stream().map((item) -> {
            SearchResultVo.catalogVo catalogVo = new SearchResultVo.catalogVo();
            ParsedStringTerms catalogName_agg = item.getAggregations().get("catalogName_agg");
            String catalogName = catalogName_agg.getBuckets().get(0).getKeyAsString();
            catalogVo.setCatalogId(Long.parseLong(item.getKeyAsString()));
            catalogVo.setCatalogName(catalogName);
            return catalogVo;
        }).collect(Collectors.toList());
        resultVo.setCatalogs(catalogs);

        ParsedLongTerms brand_agg = response.getAggregations().get("brand_agg");
        List<SearchResultVo.BrandVo> brands = brand_agg.getBuckets().stream().map((item) -> {
            SearchResultVo.BrandVo brandVo = new SearchResultVo.BrandVo();
            ParsedStringTerms brandImage_agg = item.getAggregations().get("brandImage_agg");
            ParsedStringTerms brandName_agg = item.getAggregations().get("brandName_agg");
            brandVo.setBrandId(Long.parseLong(item.getKeyAsString()));
            brandVo.setBrandName(brandName_agg.getBuckets().get(0).getKeyAsString());
            brandVo.setBrandImage(brandImage_agg.getBuckets().get(0).getKeyAsString());
            return brandVo;
        }).collect(Collectors.toList());
        resultVo.setBrands(brands);

        ParsedNested attrs_agg = response.getAggregations().get("attrs_agg");
        ParsedLongTerms attrId_agg = attrs_agg.getAggregations().get("attrId_agg");
        List<SearchResultVo.attrVo> attrs = attrId_agg.getBuckets().stream().map((item) -> {
            SearchResultVo.attrVo attrVo = new SearchResultVo.attrVo();
            ParsedStringTerms attrName_agg = item.getAggregations().get("attrName_agg");
            ParsedStringTerms attrValue_agg = item.getAggregations().get("attrValue_agg");
            List<String> attrValue = attrValue_agg.getBuckets().stream().map((image) -> {
                return image.getKeyAsString();
            }).collect(Collectors.toList());
            attrVo.setAttrId(Long.parseLong(item.getKeyAsString()));
            attrVo.setAttrName(attrName_agg.getBuckets().get(0).getKeyAsString());
            attrVo.setAttrValue(attrValue);
            return attrVo;
        }).collect(Collectors.toList());
        resultVo.setAttrs(attrs);
        return resultVo;

     /*   private List<SkuEsModel>  products;
        // 分页信息
        private Integer pageNum;
        private Long total;
        private Integer totalPage;
        private List<BrandVo> brands;
        private List<attrVo> attrs;

        @Data
        public static class BrandVo{
            private Long brandId;
            private String brandName;
            private String brandImage;
        }

        @Data
        public static class attrVo{
            private Long attrId;
            private String attrName;
            private List<String> attrValue;
        }
        @Data
        public static class catalogVo{
            private Long catalogId;
            private String catalogName;
            private String catalogIImage;
        }
        */

    }
}
