package com.shiyulong.config;

import lombok.Data;

/**
 * @author 石玉龙 at 2025/2/14 11:29
 */
@Data
public class RedissonPoolProperties {


    private int maxIdle;
    private int minIdle;
    private int maxActive;
    private int maxWait;
    private int connTimeout;
    private int soTimeout;

    private int size;


}
