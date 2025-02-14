package com.shiyulong;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

/**
 * Guava布隆过滤器，helloworld入门演示
 * @author 石玉龙 at 2025/2/6 11:11
 */
public class GuavaTest {
    public static void main(String[] args) {
        // 1 创建Guava 版本布隆过滤器(数据为integer类型，初始化数据量为100)
        BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(), 100);
        // 2 判断指定的元素是否存在
        System.out.println(bloomFilter.mightContain(1));// false
        System.out.println(bloomFilter.mightContain(2));// false
        System.out.println();
        // 3 将元素新增进入布隆过滤器
        bloomFilter.put(1);
        bloomFilter.put(2);
        System.out.println(bloomFilter.mightContain(1));// true
        System.out.println(bloomFilter.mightContain(2));// true
    }
}
