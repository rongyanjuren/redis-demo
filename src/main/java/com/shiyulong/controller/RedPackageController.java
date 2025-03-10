package com.shiyulong.controller;

import cn.hutool.core.util.IdUtil;
import com.google.common.primitives.Ints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;


@RestController
public class RedPackageController {
    /**
     * 抢红包
     * 需求分析
     * 各种节假日，发红包+抢红包，不说了，100%高并发业务要求，不能用mysql来做
     * 一个总的大红包，会有可能拆分成多个小红包，总金额 = 分金额1+分金额2+分金额3......分金额N
     * 每个人只能抢一次，你需要有记录，比如100块钱，被拆分成10个红包发出去，总计有10个红包，抢一个少一个，总数显示(10/6)直到完，需要记录哪些人抢到了红包，重复抢作弊不可以。
     * 有可能还需要你计时，完整抢完，从发出到全部over，耗时多少?
     * 红包过期，或者群主人品差，没人抢红包，原封不动退回。
     * 红包过期，剩余金额可能需要回退到发红包主账户下。
     * 由于是高并发不能用mysql来做，只能用redis，那需要要redis的什么数据类型?
     * 架构设计
     * 难点：
     *      1 拆分算法如何 红包其实就是金额，拆分算法如何 ?给你100块，分成10个小红包(金额有可能小概率相同，有2个红包都是2.58)如何拆分随机金额设定每个红包里面安装多少钱?
     *      2 次数限制 每个人只能抢一次，次数限制
     *       3 原子性3 每抢走一个红包就减少一个(类似减库存)，那这个就需要保证库存的-----------------------原子性，但是还需要不加锁实现，因为大量并发上来后，全部都卡死的话，用户使用效果不佳，并且容易导致服务器被压垮

     * 关键点
     * 发红包
     * 抢红包
     * 1、 抢，不加锁且原子性，还能支持高并发
     * 2、 没人一次且有抢红包记录
     * 记红包
     * 1、 记录每个人抢了多少 hash
     * 拆红包

     * 所有人抢到金额之和等于红包金额，不能超过，也不能少于。
     * 每个人至少抢到一分钱
     * 要保证所有人抢到金额的几率相等

     *  抢红包业务通用算法：二倍均值法
     *  剩余红包金额为M，剩余人数为N，那么有如下公式:
     *  每次抢到的金额 = 随机区间 (0，(剩余红包金额M ÷ 剩余人数N )X 2)
     * 这个公式，保证了每次随机金额的平均值是相等的，不会因为抢红包的先后顺序而造成不公平。
     * 举个栗子: 假设有10个人，红包总额100元。
     * 第1次: 100 ÷ 10 X 2 = 20,所以第一个人的随机范围是 (0，20 )，平均可以抢到10元。假设第一个人随机到10元，那么剩余金额是100-10 = 90
     * 第2次: 909 X2 = 20,所以第二个人的随机范围同样是 (0，20 )，平均可以抢到10元。假设第二个人随机到10元，那么剩余金额是90-10 = 80 元。
     * 第3次: 80-8 X2 = 20,所以第三个人的随机范围同样是 (0，20 )，平均可以抢到10元。 以此类推，每一次随机范围的均值是相等的。
     */
    public static final String RED_PACKAGE_KEY = "redpackage:";
    public static final String RED_PACKAGE_CONSUME_KEY = "redpackage:consume";

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 发红包
     * 用list存储，每次抢一个就随机吐出一个
     * @param totalMoney 总金额
     * @param redpackageNumber 红包个数
     * @return
     */
    @RequestMapping(value = "/send")
    public String sendRedPackage(int totalMoney, int redpackageNumber) {
        // 1 拆红包将总金额totalMoney拆分成redpackageNumber个子红包
        Integer[] splitRedPackages = splitRedPackageAlgorithm(totalMoney, redpackageNumber);
        // 2 发红包并保存进list结构里面且设置过期时间
        String key = RED_PACKAGE_KEY + IdUtil.simpleUUID();
        redisTemplate.opsForList().leftPushAll(key, splitRedPackages);
        redisTemplate.expire(key, 1, TimeUnit.DAYS);

        return key + "\t" + Ints.asList(Arrays.stream(splitRedPackages).mapToInt(Integer::valueOf).toArray());
    }

    /**
     * 抢红包
     *
     * @param redpackageKey redpackageKey
     * @param userId 用户id
     * @return
     */
    @RequestMapping(value = "/rob")
    public String robRedPackage(String redpackageKey, String userId) {
        // 验证某个用户是否抢过红包，不可以多抢
        Object redPackage = redisTemplate.opsForHash().get(RED_PACKAGE_CONSUME_KEY + redpackageKey, userId);
        if (redPackage == null) {
            // 红包没有抢完才能让用户接着抢
            Object partRedPackage = redisTemplate.opsForList().leftPop(RED_PACKAGE_KEY + redpackageKey);
            if(partRedPackage != null) {
                redisTemplate.opsForHash().put(RED_PACKAGE_CONSUME_KEY+redpackageKey, userId, partRedPackage);
                System.out.println("用户：" + userId + "\t 抢到了" + partRedPackage + "");
                // TODO 后续异步操作（年度总结）或者回滚操作
                return String.valueOf(partRedPackage);
            }
            // 抢完了
            return "errorCode:-1, 红包抢完了";
        }
        // 抢过了，不能抢多次
        return "errorCode:-2," + userId + "\t已经抢过了";

    }

    // 拆分红包算法 --》 二倍均值算法
    private Integer[] splitRedPackageAlgorithm(int totalMoney, int redpackageNumber) {
        Integer[] redpackageNumbers = new Integer[redpackageNumber];
        // 已经被抢夺的红包金额
        int useMoney = 0;
        for (int i = 0; i < redpackageNumber; i++) {
            if (i == redpackageNumber - 1) {
                redpackageNumbers[i] = totalMoney - useMoney;
            } else {
                // 二倍均值算法，每次拆分后塞进子红包的金额
                // 金额 = 随机区间(0，(剩余红包金额M ÷ 剩余人数N ) * 2)
                int avgMoney = ((totalMoney - useMoney) / (redpackageNumber - i)) * 2;
                redpackageNumbers[i] = 1 + new Random().nextInt(avgMoney - 1);
                useMoney = useMoney + redpackageNumbers[i];
            }
        }
        return redpackageNumbers;
    }
}