package com.oaec.tourism.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.util.Date;

@Data
public class User{
    private Integer id;
    @NotBlank(message = "电话不能为空!")
    @Pattern(regexp = "^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$",message = "手机号码格式不正确")
    private String phone; //电话
    private String password;//密码
    private String salt;//加盐加密
    private String city;//所在城市
    @NotBlank(message = "用户名不能为空!")
    private String username;//用户名
    @NotNull(message = "性别不能为空")
    private Integer sex;//性别 2:保密 | 1：男 | 0:女
    private String imgPath;//用户头像
    @Email
    private String email;//邮件
    private String realName;//真实姓名
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;//生日
    private Date createTime;//注册时间
 }
