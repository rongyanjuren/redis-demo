package com.shiyulong.service;

import com.shiyulong.po.Customer;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 石玉龙
 * @since 2025-01-23
 */
public interface CustomerService extends IService<Customer> {

    void addCustomer(Customer customer);

    Customer findCustomerById(Integer id);

    Customer findCustomerByIdWithBloomFilter(Integer id);
}
