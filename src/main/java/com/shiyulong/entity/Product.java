package com.shiyulong.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 聚划算活动Product信息
 * @author 石玉龙 at 2025/2/7 11:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    // 产品id
    private Long id;
    // 产品名称
    private String name;
    // 产品价格
    private Integer price;
    // 产品详情
    private String detail;

}