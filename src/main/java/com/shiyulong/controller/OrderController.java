package com.shiyulong.controller;


import com.shiyulong.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedissonClient redissonClient;

    @PostMapping("/order/add")
    public String addOrder() {
        orderService.addOrder();
        return "success";
    }

    @GetMapping("/order/query")
    public String queryOrder(Integer keyId) {
        return orderService.getOrderById(keyId);
    }

    @GetMapping("/testRedission")
    public String testRedission() throws InterruptedException {
        RLock lock = redissonClient.getLock("bb");
        boolean b = lock.tryLock();
        System.out.println("是否获取到了bb锁"+b);
        if (b) {
            Thread.sleep(5000);
            lock.unlock();
            return "获取到了锁";
        }
        return "没获取锁";
    }

    @GetMapping("/testRedission2")
    public String testRedission2() {
        RLock lock = redissonClient.getLock("bb");
        boolean b = lock.tryLock();
        System.out.println("是否获取到了bb锁"+b);
        if (b) {
            lock.unlock();
            return "获取到了锁";
        }
        return "没获取锁";
    }
}
