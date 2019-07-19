package com.oaec.tourism.entity;

import lombok.Data;

/*主题*/
@Data
public class Theme {
    private Integer id;
    private String name;
    private String detail;//概述
    private Integer type;//1回收站 0列表
}
