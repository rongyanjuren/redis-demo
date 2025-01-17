package com.shiyulong.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 石玉龙 at 2025/1/16 17:51
 */
@Configuration
public class RedissonConfig {

    @Value("${spring.redis.redisson.mode}")
    private String mode;

    private static final String REDIS = "redis://";

    private static final String REDIS_START_WITH = "//:";

    public static final String PROFANITY_STR = ":";

    @Bean
    public RedissonClient redissonClient(RedisProperties redisProperties) {
        Config config = new Config();
        if ("single".equals(mode)) {
            config.useSingleServer()
                    .setDatabase(redisProperties.getDatabase())
                    .setPassword(redisProperties.getPassword())
                    .setAddress(REDIS + redisProperties.getHost() + PROFANITY_STR + redisProperties.getPort());
        } else if ("cluster".equals(mode)) {
            String[] address = redisProperties.getCluster().getNodes().stream().map(w -> w.startsWith(REDIS) ? w : REDIS + w).toArray(String[]::new);
            config.useClusterServers()
                    //集群模式不支持多个数据库概念，默认db 0
                    .setPassword(redisProperties.getPassword())
                    .addNodeAddress(address);
        } else if ("sentinel".equals(mode)) {
            RedisProperties.Sentinel sentinel = redisProperties.getSentinel();
            String[] address = sentinel.getNodes().stream().map(w -> w.startsWith(REDIS_START_WITH) ? w : REDIS + w).toArray(String[]::new);
            config.useSentinelServers()
                    .setDatabase(redisProperties.getDatabase())
                    .setPassword(sentinel.getPassword())
                    .setMasterName(sentinel.getMaster())
                    .addSentinelAddress(address);

        } else if ("master-slave".equals(mode)) {
            String[] address = redisProperties.getCluster().getNodes().stream().map(w -> w.startsWith(REDIS) ? w : REDIS + w).toArray(String[]::new);
            if (address.length == 1) {
                throw new IllegalArgumentException(
                        "redis.redisson.address MUST have multiple redis addresses for master-slave mode.");
            }
            String[] slaveAddresses = new String[address.length - 1];
            System.arraycopy(address, 1, slaveAddresses, 0, slaveAddresses.length);
            config.useMasterSlaveServers()
                    .setDatabase(redisProperties.getDatabase())
                    .setPassword(redisProperties.getPassword())
                    .setMasterAddress(address[0])
                    .addSlaveAddress(slaveAddresses);
        } else {
            throw new IllegalArgumentException(mode);
        }
        return Redisson.create(config);
    }

}
