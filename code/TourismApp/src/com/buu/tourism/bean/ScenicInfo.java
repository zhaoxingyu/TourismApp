package com.buu.tourism.bean;
/**
 * 景区信息实体类
 * @author xingyu10
 *
 */
public class ScenicInfo extends Bean {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * 景区ID
     */
    public int sId;
    /**
     * 景区名称
     */
    public String scenicName;
    /**
     * 景区别名
     */
    public String scenicNameAlias;
    /**
     * 景区地址
     */
    public String scenicAddr;
    /**
     * 经度
     */
    public String lng;
    /**
     * 维度
     */
    public String lat;
    /**
     * 景区描述
     */
    public String desc;
    /**
     * 景区海报
     */
    public String posterUrl;
    /**
     * 景区距离（单位：m）
     */
    public long distance;
    /**
     * 活动开始时间
     */
    public long promotionBegin;
    /**
     * 活动结束时间
     */
    public long promotionEnd;
    /**
     * 价格
     */
    public double price;
    /**
     * 简介
     */
    public String intro;
    /**
     * 商家电话
     */
    public String phoneNumber;

}
