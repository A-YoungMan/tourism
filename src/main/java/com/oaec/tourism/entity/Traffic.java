package com.oaec.tourism.entity;

import lombok.Data;

import java.util.Date;

/*往返交通*/
@Data
public class Traffic {
    private Integer id;
    private String goPoint;//出发上车点
    private Date goTime;//出发时间
    private String returnPoint;//返回上车点
    private Date returnTime;//返回发车时间
}
