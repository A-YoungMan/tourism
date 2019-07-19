package com.oaec.tourism.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Manager {
    private Integer id;
    private String account;//账号
    private String username;
    private String password;
    private String salt;//加盐加密
    private Date createTime;
    private Integer type;//类型 1:超级管理员 | 0:普通管理员
}
