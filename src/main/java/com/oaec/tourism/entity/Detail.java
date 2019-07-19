package com.oaec.tourism.entity;

import lombok.Data;

/*产品详情页*/
@Data
public class Detail {
    private Integer id;
    private Trip trip;//所属景点类型
    private String place;//景点介绍
    private String hotel;//酒店介绍
    private String food;//美食介绍
}
