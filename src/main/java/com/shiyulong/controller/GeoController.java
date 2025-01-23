package com.shiyulong.controller;

import com.shiyulong.service.GeoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 石玉龙 at 2025/1/22 11:07
 */
@RestController
@Slf4j
@RequestMapping("geo")
public class GeoController {


    @Autowired
    private GeoService geoService;

    /**
     * 添加坐标
     *
     * @return
     */
    @GetMapping("geoadd")
    public String geoAdd() {
        return geoService.geoAdd();
    }

    /**
     * 获取经纬度坐标
     *
     * @param member
     * @return
     */
    @GetMapping("geopos")
    public Point getPoint(String member) {
        return geoService.position(member);
    }

    /**
     * 获取经纬度生成的base32编码值
     *
     * @param member
     * @return
     */
    @GetMapping("geohash")
    public String hash(String member) {
        return geoService.hash(member);
    }

    /**
     * 获取两个给定位置之间的距离
     *
     * @param member1
     * @param member2
     * @return
     */
    @GetMapping("geodist")
    public Distance distance(String member1, String member2) {
        return geoService.distance(member1, member2);
    }


    /**
     * 通过经纬度查找北京王府井附近
     *
     * @return
     */
    @GetMapping("georadius")
    public GeoResults radiusByxy() {
        return geoService.radiusByxy();
    }

    /**
     * 通过地方查找附近，本案例写死天安门作为地址
     *
     * @return
     */
    @GetMapping("georadiusByMember")
    public GeoResults radiusByMember() {
        return geoService.radiusByMember();
    }
}
