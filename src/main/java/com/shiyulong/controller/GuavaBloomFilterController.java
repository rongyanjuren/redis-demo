package com.shiyulong.controller;

import com.shiyulong.service.GuavaBloomFilterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * google工具Guava处理布隆过滤器
 */
@RestController
@Slf4j
public class GuavaBloomFilterController {

    @Autowired
    private GuavaBloomFilterService guavaBloomFilterService;

    /**
     * guava布隆过滤器插入100万样本数据并额外10万测试是否存在
     */
    @GetMapping("/guavafilter")
    public void guavaBloomFIlterService() {
        guavaBloomFilterService.guavaBloomFilterService();
    }

}
