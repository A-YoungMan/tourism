package com.oaec.tourism.entity;

import lombok.Data;

/**
 * 行程Trip日程实体类
 */
@Data
public class Dates {
    private Integer id;
    private Integer tripId;//景区项目id
    private String title;//标题
    private Integer num;//第几天
    private String detail;//日程明细
    private String hotel;//酒店
    private String meal;//用餐
    private String traffic;//交通
    private String prompt;//温馨提示
}
