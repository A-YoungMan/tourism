package com.oaec.tourism.entity;

import lombok.Data;

/*景点坐标*/
@Data
public class Position {
    private Integer id;
    private Place place;//所属景点
    private Float p1;//经度
    private Float p2;//维度
}
