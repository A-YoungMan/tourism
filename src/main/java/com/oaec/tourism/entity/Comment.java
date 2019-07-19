package com.oaec.tourism.entity;

import lombok.Data;

import java.util.Date;

/**
 * 用户点评表
 */
@Data
public class Comment {
    private Integer id;
    private User user;
    private Trip trip;//所属景点的评论
    private Order order;
    private Integer place; //0~5  景点评论
    private Integer hotel;//酒店
    private Integer service;//服务
    private Integer traffic;//交通
    private String content;//评论内容
    private Date createTime;//评论时间
    private Integer type ; //总体评价 0好评 1一般 2差
}
