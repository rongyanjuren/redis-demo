package com.shiyulong.controller;

import com.shiyulong.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * redis分布式锁测试
 */
@RestController
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;


    /**
     * 扣减库存，一次卖一个
     * @return
     */
    @GetMapping("/inventory/sale")
    public String sale() {
        return inventoryService.sale();
    }

    /**
     * redisson扣减库存，一次卖一个
     * @return
     */
    @GetMapping("/inventory/saleByRedisson")
    public String saleByRedisson() {
        return inventoryService.saleByRedisson();
    }
}
