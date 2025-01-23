package com.shiyulong.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author 石玉龙 at 2025/1/22 10:59
 */
 @Service
public class GeoService {

     @Autowired
    RedisTemplate<String,Object> redisTemplate;

     private final static String CITY = "city";


    public String geoAdd() {
        Map<Object,Point> map = new HashMap<>();
        map.put("天安门", new Point(116.403963,39.915119));
        map.put("故宫", new Point( 116.403414,  39.924091));
        map.put("长城", new Point(  116.024067,  40.362639));
        redisTemplate.opsForGeo().add(CITY,map);
        return map.toString();
    }

    public Point position(String member){
        // 获取经纬度坐标,这里的member可以有多个，返回的list也可以有多个
        List<Point> list = redisTemplate.opsForGeo().position(CITY, member);
        return list.get(0);
    }

    public String hash(String member) {
        //geohash算法生成的base32编码值,这里的member可以有多个，返回的list也可以有多个
        List<String> list = redisTemplate.opsForGeo().hash(CITY,member);
        return list.get(0);
    }

    public Distance distance(String member1, String member2) {
        //获取两个给定位置之间的距离
        Distance distance = redisTemplate.opsForGeo().distance(CITY,member1,member2, RedisGeoCommands.DistanceUnit.KILOMETERS);
        return distance;
    }

    public GeoResults radiusByxy() {
        //通过经度，纬度查找附近的，北京王府井位置116.418017, 39.914402
        Circle circle = new Circle(116.418017, 39.914402, Metrics.KILOMETERS.getMultiplier());
        //返回50条:
        RedisGeoCommands.GeoRadiusCommandArgs args =
                RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().includeCoordinates().sortAscending().limit(50);
        GeoResults<RedisGeoCommands.GeoLocation<Object>> geoResults=
                this.redisTemplate.opsForGeo().radius(CITY, circle, args);
        return geoResults;
    }

    public GeoResults radiusByMember() {
        //通过地方查找附近
        String member="天安门";
        //返回50条
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().includeCoordinates( ).sortAscending().limit(50) ;
        //半径10公里内
        Distance distance=new Distance(10,Metrics.KILOMETERS);
        GeoResults<RedisGeoCommands.GeoLocation<Object>> geoResults= this.redisTemplate.opsForGeo().radius(CITY,member,distance,args);
        return geoResults;
    }

}
