package com.oaec.tourism.entity;

import lombok.Data;

/*旅游图片*/
@Data
public class TripPicture {
    private Integer id;
    private Integer tripId;//所属的景区Id
    private String name;//图片名称
    private String base64;//转换为Base64类型
    private Integer type;//图片类型 0主图 1子图
}
