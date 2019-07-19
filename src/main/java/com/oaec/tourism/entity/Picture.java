package com.oaec.tourism.entity;

import lombok.Data;

/**
 * 旅游图片表
 */
@Data
public class Picture {
    private Integer id;
    private Trip trip;
    private String name;//名称
    private String data;//数据类型
    private Integer type;//图片类型 0：主图 1：子图
}
