package com.panpan.gulimall.essearch.service;

import com.panpan.gulimall.essearch.vo.SearchParamVo;
import com.panpan.gulimall.essearch.vo.SearchResultVo;

/**
 * @author panpan
 * @create 2021-09-01 下午9:33
 */
public interface MallSearchService {
    SearchResultVo search(SearchParamVo params);
}
