package com.shiyulong.controller;

import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author 石玉龙 at 2025/2/14 11:45
 */
@Slf4j
@RestController
@RequestMapping("redlock")
public class RedlockController {


    public static final String CACHE_KEY_REDLOCK = "redlock";


    @Autowired
    @Qualifier("redissonClient1")
    private RedissonClient redissonClient1;
    @Autowired
    @Qualifier("redissonClient2")
    private RedissonClient redissonClient2;
    @Autowired
    @Qualifier("redissonClient3")
    private RedissonClient redissonClient3;



    @GetMapping("multiLock")
    public String getMultiLock(){
        String threadId = Thread.currentThread().getId()+"";
        RLock lock1 = redissonClient1.getLock(CACHE_KEY_REDLOCK);
        RLock lock2 = redissonClient2.getLock(CACHE_KEY_REDLOCK);
        RLock lock3 = redissonClient3.getLock(CACHE_KEY_REDLOCK);
        RedissonMultiLock redissonMultiLock = new RedissonMultiLock(lock1, lock2, lock3);

        redissonMultiLock.lock();
        try {
            log.info("“进入业务中:{}",threadId);

            TimeUnit.SECONDS.sleep(30);
            log.info("“业务执行完成:{}",threadId);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("“业务执行出错:{}",threadId);
        }finally {
            redissonMultiLock.unlock();
            log.info("释放锁:{}",CACHE_KEY_REDLOCK);
        }

        return "当前线程id"+threadId;
    }



}
