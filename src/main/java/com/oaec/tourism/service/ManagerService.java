package com.oaec.tourism.service;

import com.oaec.tourism.entity.Manager;

import java.util.List;
import java.util.Map;

public interface ManagerService {
    Map<String,Object> loginAdmin(String account, String password);// 管理员登录
    Map<String,Object> register(String account,String username,String password);//新增管理员
    int updateAdmin(Manager manager); //修改
    int delete(int id);
    Manager findByAccount(String account);
    Manager findById(int id);
    List<Manager> findAll();
    Map<String,Object> addAdmin(Manager manager);
}
