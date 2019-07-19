package com.oaec.tourism.entity;

import lombok.Data;

import java.util.Date;

/**
 * 订单序列表
 */
@Data
public class Sequence {
    private Integer id;
    private String value;   //value
    private String keying; // key
    private Integer type; //状态
    private String descing;//说明
    /*下面使用临时存储*/
    private Date startDate;//订单出行日期
    private String goTime;//上车出行时间
    private String goPoint;//临时存储出行地点
    private Float price;//价格
    private Integer number;//出游人数
}
