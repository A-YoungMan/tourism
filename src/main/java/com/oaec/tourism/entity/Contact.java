package com.oaec.tourism.entity;

import lombok.Data;

import java.util.Date;

/*常用联系人*/
@Data
public class Contact {
    private Integer id;
    private User user;//所属用户
    private String name;//用户真实姓名
    private String phone;//手机号码
    private Integer sex;//1男 0女
    private String email;//邮件
    private String cardno;//身份证
    private Date birthday;//生日
}
