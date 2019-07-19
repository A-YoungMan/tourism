package com.oaec.tourism.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 用户订单表
 */
@Data
public class Order {
    private Integer id;
    private String orderNo;//编号
    private Trip trip;//所属景点项目
    private User user;//当前用户下单
    private Integer adultNum;//成人人数
    private Integer childNum;//儿童人数
    private Date startDate;//出发日期
    private Date createDate;//下单时间
    private String goPoint;//发车地点
    private String goTime;//出发时间
    private Double totalPrice;//总价
    private List<Contact> contacts;
}
