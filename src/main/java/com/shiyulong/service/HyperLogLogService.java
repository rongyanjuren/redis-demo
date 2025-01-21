package com.shiyulong.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Random;

/**
 * @author 石玉龙 at 2025/1/21 17:48
 */
@Service
@Slf4j
public class HyperLogLogService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @PostConstruct
    public void initIP() {
        new Thread(() -> {
            for (int i = 0; i < 300; i++) {
                Random random = new Random();

               String ip =  random.nextInt(256)+"."+
                random.nextInt(256)+"."+
                random.nextInt(256)+"."+
                random.nextInt(256);
                Long hll = redisTemplate.opsForHyperLogLog().add("hll", ip);
                log.info("ip={},该ip访问首页的次数:{}",ip,hll);
            }
        }).start();
    }

    public Long getUv(){
        return redisTemplate.opsForHyperLogLog().size("hll");
    }

}
