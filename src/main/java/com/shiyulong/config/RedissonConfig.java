package com.shiyulong.config;

import cn.hutool.core.util.StrUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author 石玉龙 at 2025/1/16 17:51
 */
@Configuration
@EnableConfigurationProperties(RedissonProperties.class)
public class RedissonConfig {

//    @Value("${spring.redis.redisson.mode}")
//    private String mode;

    private static final String REDIS = "redis://";

    private static final String REDIS_START_WITH = "//:";

    public static final String PROFANITY_STR = ":";


    @Autowired
    private RedissonProperties redissonProperties;




//    /**
//     * redisson锁和业务redis用同一个，真实的案例中不推荐这么用
//     *
//     * @param redisProperties
//     * @return
//     */
//    @Bean
//    public RedissonClient redissonClient(RedisProperties redisProperties) {
//        Config config = new Config();
//        if ("single".equals(mode)) {
//            config.useSingleServer()
//                    .setDatabase(redisProperties.getDatabase())
//                    .setPassword(redisProperties.getPassword())
//                    .setAddress(REDIS + redisProperties.getHost() + PROFANITY_STR + redisProperties.getPort());
//        } else if ("cluster".equals(mode)) {
//            String[] address = redisProperties.getCluster().getNodes().stream().map(w -> w.startsWith(REDIS) ? w : REDIS + w).toArray(String[]::new);
//            config.useClusterServers()
//                    //集群模式不支持多个数据库概念，默认db 0
//                    .setPassword(redisProperties.getPassword())
//                    .addNodeAddress(address);
//        } else if ("sentinel".equals(mode)) {
//            RedisProperties.Sentinel sentinel = redisProperties.getSentinel();
//            String[] address = sentinel.getNodes().stream().map(w -> w.startsWith(REDIS_START_WITH) ? w : REDIS + w).toArray(String[]::new);
//            config.useSentinelServers()
//                    .setDatabase(redisProperties.getDatabase())
//                    .setPassword(sentinel.getPassword())
//                    .setMasterName(sentinel.getMaster())
//                    .addSentinelAddress(address);
//
//            //如果只用主从会有主节点宕机的风险，如果用主从建议用哨兵模式
//        } else if ("master-slave".equals(mode)) {
//            String[] address = redisProperties.getCluster().getNodes().stream().map(w -> w.startsWith(REDIS) ? w : REDIS + w).toArray(String[]::new);
//            if (address.length == 1) {
//                throw new IllegalArgumentException(
//                        "redis.redisson.address MUST have multiple redis addresses for master-slave mode.");
//            }
//            String[] slaveAddresses = new String[address.length - 1];
//            System.arraycopy(address, 1, slaveAddresses, 0, slaveAddresses.length);
//            config.useMasterSlaveServers()
//                    .setDatabase(redisProperties.getDatabase())
//                    .setPassword(redisProperties.getPassword())
//                    .setMasterAddress(address[0])
//                    .addSlaveAddress(slaveAddresses);
//        } else {
//            throw new IllegalArgumentException(mode);
//        }
//        return Redisson.create(config);
//    }

    @Bean(name = "redissonClient")
    public RedissonClient redissonClient() {
        Config config = new Config();
        String node = redissonProperties.getSingle().getAddress1();
        node = node.startsWith(REDIS)?node:REDIS+node;
        SingleServerConfig singleServerConfig = config.useSingleServer()
                .setAddress(node);
//                .setTimeout(redissonProperties.getPool().getConnTimeout())
//                .setConnectionPoolSize(redissonProperties.getPool().getSize())
//                .setConnectionMinimumIdleSize(redissonProperties.getPool().getMinIdle());
        if (StrUtil.isNotEmpty(redissonProperties.getPassword())) {
            singleServerConfig.setPassword(redissonProperties.getPassword());
        }
        return Redisson.create(config);
    }

    @Bean(name = "redissonClient1")
    public RedissonClient redissonClient1() {
        Config config = new Config();
        String node = redissonProperties.getSingle().getAddress1();
        node = node.startsWith(REDIS)?node:REDIS+node;
        SingleServerConfig singleServerConfig = config.useSingleServer()
                .setAddress(node);
//                .setTimeout(redissonProperties.getPool().getConnTimeout())
//                .setConnectionPoolSize(redissonProperties.getPool().getSize())
//                .setConnectionMinimumIdleSize(redissonProperties.getPool().getMinIdle());
        if (StrUtil.isNotEmpty(redissonProperties.getPassword())) {
            singleServerConfig.setPassword(redissonProperties.getPassword());
        }
        return Redisson.create(config);
    }
    @Bean(name = "redissonClient2")
    public RedissonClient redissonClient2() {
        Config config = new Config();
        String node = redissonProperties.getSingle().getAddress2();
        node = node.startsWith(REDIS)?node:REDIS+node;
        SingleServerConfig singleServerConfig = config.useSingleServer()
                .setAddress(node);
//                .setTimeout(redissonProperties.getPool().getConnTimeout())
//                .setConnectionPoolSize(redissonProperties.getPool().getSize())
//                .setConnectionMinimumIdleSize(redissonProperties.getPool().getMinIdle());
        if (StrUtil.isNotEmpty(redissonProperties.getPassword())) {
            singleServerConfig.setPassword(redissonProperties.getPassword());
        }
        return Redisson.create(config);
    }

    @Bean(name = "redissonClient3")
    public RedissonClient redissonClient3() {
        Config config = new Config();
        String node = redissonProperties.getSingle().getAddress3();
        node = node.startsWith(REDIS)?node:REDIS+node;
        SingleServerConfig singleServerConfig = config.useSingleServer()
                .setAddress(node);
//                .setTimeout(redissonProperties.getPool().getConnTimeout())
//                .setConnectionPoolSize(redissonProperties.getPool().getSize())
//                .setConnectionMinimumIdleSize(redissonProperties.getPool().getMinIdle());
        if (StrUtil.isNotEmpty(redissonProperties.getPassword())) {
            singleServerConfig.setPassword(redissonProperties.getPassword());
        }
        return Redisson.create(config);
    }
}
