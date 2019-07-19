package com.oaec.tourism.entity;

import lombok.Data;
/*城市*/
@Data
public class Region {
    private String id;
    private String name;
    private String nameCn;
    private Integer pid;
}
