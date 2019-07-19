package com.oaec.tourism.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

/*景点表*/
@Data
public class Place {
    private Integer id;
    private String name;//名称
    private City city;//所属地区
    private String area;//城市
    private Position position;//经纬度
    private Integer type;//是否0显示,1删除
    private Date createTime;//创建时间
    private List<Trip> trips;
}
