package com.oaec.tourism.entity;

import lombok.Data;

/*城市类*/
@Data
public class City {
    private Integer id;
    private City city;//上级城市
    private Integer type;//类型0：国家  1：省份  2：城市  3：县城
    private String name;//名称
}
