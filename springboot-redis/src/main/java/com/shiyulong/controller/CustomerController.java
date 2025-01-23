package com.shiyulong.controller;


import com.shiyulong.po.Customer;
import com.shiyulong.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 石玉龙
 * @since 2025-01-23
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {


    @Autowired
    private CustomerService customerService;

    /**
     * 数据库初始化两条Customer记录
     */
    @PostMapping(value = "/customer/add")
    public void addCustomer() {
        for (int i = 0; i < 2; i++) {
            Customer customer = new Customer();
            customer.setCname("customer" + i);
            customer.setAge(new Random().nextInt(30) + 1);
            customer.setPhone("139546556");
            customer.setSex(new Random().nextInt(2));
            customer.setBirth(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            customerService.addCustomer(customer);
        }
    }

    /**
     * 单个customer查询操作
     * @param id
     * @return
     */
    @PostMapping(value = "/customer/{id}")
    public Customer findCustomerById(@PathVariable Integer id) {
        return customerService.findCustomerById(id);
    }

    /**
     * BloomFilter, 单个customer查询操作
     * @param id
     * @return
     */
    @PostMapping(value = "/customerBloomFilter/{id}")
    public Customer findCustomerByIdWithBloomFilter(@PathVariable Integer id) {
        return customerService.findCustomerByIdWithBloomFilter(id);
    }



}

