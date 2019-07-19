package com.oaec.tourism.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*旅游表*/
@Data
public class Trip {
    private Integer id;
    private Integer num;//产品编号
    private String title;//标题
    private String sTitle;//小标题
    private City city;//出发地
    private String temporary;//临时存储
    private Integer type;//行程类型0：自驾游  1：国内游 2：出境游（Sequence：ID）
    private String traffic;//交通规格
    private String hotel;//酒店规格
    private Integer time;//出行天数
    private Float goodRate;//好评率
    private Float placeScore;//景点好评率
    private Float hotelScore;//酒店好评率
    private Float serviceScore;//服务评分
    private Float trafficScore;//交通评分
    private Integer isOk;//是否上架 0是 1否
    private Integer dType;//是否删除 0否，1是加入回收站
    private Date createTime;
    private Place place;
    private List<TripPicture> tripPictures;
    private List<Price> prices;
    private List<Theme> themes;
}
