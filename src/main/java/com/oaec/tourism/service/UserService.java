package com.oaec.tourism.service;

import com.oaec.tourism.entity.User;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

public interface UserService {
    Map<String,Object> login(String phone, String password);//登录
    //用户注册
    Map<String,Object> register(String phone,String password,String username,int sex,String email);
    Map<String,Object> create(User user);
    List<User> findAll();
    User findById(int id);//查找用户
    @Resource
    User findByUserPhone(String phone);//账户查找
    User findByCommentUserId(int id);
    int updateUserInfo(User user);//修改用户信息

}
