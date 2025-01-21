package com.shiyulong.controller;

import com.shiyulong.service.HyperLogLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 石玉龙 at 2025/1/21 19:00
 */
@RestController
@RequestMapping("hll")
@Slf4j
public class HyperLogLogController {


    @Autowired
    private HyperLogLogService hyperLogLogService;

    /**
     * 淘宝亿级UV的redis统计方案
     * @return
     */
    @PostMapping("/getUv")
    public Long addOrder() {
        return  hyperLogLogService.getUv();
    }

}
