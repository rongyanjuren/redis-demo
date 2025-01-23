package com.shiyulong.service.impl;

import com.shiyulong.po.Customer;
import com.shiyulong.mapper.CustomerMapper;
import com.shiyulong.service.CustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyulong.utils.CheckUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 石玉龙
 * @since 2025-01-23
 */
@Slf4j
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

    public static final String CACHE_KEY_CUSTOMER = "customer:";

    @Resource
    private CustomerMapper customerMapper;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private CheckUtils checkUtils;

    public void addCustomer(Customer customer) {
        int i = customerMapper.insert(customer);
        if (i > 0) {
            // mysql插入成功，需要重新查询一次将数据捞出来，写进Redis
            Customer result = customerMapper.selectById(customer.getId());
            // redis 缓存key
            String key = CACHE_KEY_CUSTOMER + result.getId();
            redisTemplate.opsForValue().set(key, result);
        }
    }

    public Customer findCustomerById(Integer customerId) {
        Customer customer = null;
        // 缓存redis的key名称
        String key = CACHE_KEY_CUSTOMER + customerId;
        // 查看redis是否存在
        customer = (Customer) redisTemplate.opsForValue().get(key);

        // redis 不存在，取MySQL中查找
        if (null == customer) {
            // 双端加锁策略
            synchronized (CustomerService.class) {
                customer = (Customer) redisTemplate.opsForValue().get(key);
                if (null == customer) {
                    customer = customerMapper.selectById(customerId);
                    if (null == customer) {
                        // 数据库没有放入redis设置缓存过期时间
                        redisTemplate.opsForValue().set(key, customer, 60, TimeUnit.SECONDS);
                    } else {
                        redisTemplate.opsForValue().set(key, customer);
                    }
                }
            }

        }

        return customer;
    }

    /**
     * BloomFilter -> redis -> mysql
     * @param customerId
     * @return
     */
    public Customer findCustomerByIdWithBloomFilter(Integer customerId) {
        Customer customer = null;
        // 缓存redis的key名称
        String key = CACHE_KEY_CUSTOMER + customerId;

        // 布隆过滤器check
        if (!checkUtils.checkWithBloomFilter("whitelistCustomer", key)) {
            log.info("白名单无此顾客，不可以访问，{}", key);
            return null;
        }

        // 查看redis是否存在
        customer = (Customer) redisTemplate.opsForValue().get(key);
        // redis 不存在，取MySQL中查找
        if (null == customer) {
            // 双端加锁策略
            synchronized (CustomerService.class) {
                customer = (Customer) redisTemplate.opsForValue().get(key);
                if (null == customer) {
                    customer = customerMapper.selectById(customerId);
                    if (null == customer) {
                        // 数据库没有放入redis设置缓存过期时间
                        redisTemplate.opsForValue().set(key, customer, 60, TimeUnit.SECONDS);
                    } else {
                        redisTemplate.opsForValue().set(key, customer);
                    }
                }
            }

        }

        return customer;
    }

    

}
