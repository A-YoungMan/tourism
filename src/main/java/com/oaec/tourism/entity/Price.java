package com.oaec.tourism.entity;

import lombok.Data;

import java.util.Date;

/*价格表*/
@Data
public class Price {
    private Integer id;
    private Date date;//日期价格日期
    private Trip trip; //所属景点
    private Float price;//单价
}
