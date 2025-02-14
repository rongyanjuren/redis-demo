package com.shiyulong.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 石玉龙 at 2025/2/14 11:22
 */
@Data
@ConfigurationProperties(prefix = "spring.redisson")
public class RedissonProperties {

    /**
     * 等待节点回复命令的时间 该时间从命令发送成功时开始计时
     */
    private Integer timeout = 3000;

    private String password;

    private String mode;

    private RedissonPoolProperties pool;

    private RedissonSingleProperties single;

}
