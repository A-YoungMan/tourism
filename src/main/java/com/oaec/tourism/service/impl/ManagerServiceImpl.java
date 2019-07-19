package com.oaec.tourism.service.impl;

import com.oaec.tourism.entity.Manager;
import com.oaec.tourism.mapper.ManagerMapper;
import com.oaec.tourism.service.ManagerService;
import com.oaec.tourism.shiro.ShiroUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ManagerServiceImpl implements ManagerService {
    @Autowired
    private ManagerMapper managerMapper;

    @Override
    public Map<String, Object> addAdmin(Manager manager) {
        Map<String,Object> map = new HashMap<>();
        map.put("ok",false);
        String password = manager.getPassword();
        String salt = ShiroUtil.createSalt();
        password = ShiroUtil.createPwdBySalt(password,salt);
        manager.setSalt(salt);
        manager.setPassword(password);
        manager.setCreateTime(new Date());
        int i = managerMapper.create(manager);
        if(i>0){
            map.put("ok",true);
        }
        return map;
    }

    @Override
    public Map<String, Object> loginAdmin(String account, String password) {
        Map<String,Object> map = new HashMap<>();
        map.put("ok",false);
        //使用shiro的认证
        Subject subject = SecurityUtils.getSubject();
        //创建令牌
        UsernamePasswordToken token = new UsernamePasswordToken(account,password);
        try{
            subject.login(token);
            map.put("ok",true);
            //从subject中获取当前登录用户的对象
            Manager manager = (Manager) token.getPrincipal();
            map.put("manager",manager);
        }catch (UnknownAccountException e){
            map.put("error","登录失败！该管理员不存在！");
        }catch (IncorrectCredentialsException e){
            map.put("error","登录失败！密码错误！");
        }catch (Exception e){
            map.put("error","登录失败！内部错误！");
        }
        return map;
    }

    @Override
    public Map<String, Object> register(String account, String username, String password) {
        Map<String,Object> map = new HashMap<>();
        map.put("ok",false);
        Manager manager = new Manager();
        manager.setAccount(account);
        manager.setUsername(username);
        //给密码加盐加密
        String salt = ShiroUtil.createSalt();
        manager.setSalt(salt);
        password = ShiroUtil.createPwdBySalt(password, salt);
        manager.setPassword(password);
        manager.setCreateTime(new Date());
        int i = managerMapper.create(manager);
        if(i>0){
            map.put("ok",true);
        }
        return map;
    }

    @Override
    public int updateAdmin(Manager m) {
        return managerMapper.update(m);
    }

    @Override
    public int delete(int id) {
        return managerMapper.delete(id);
    }

    @Override
    public Manager findByAccount(String account) {
        return managerMapper.findByAccount(account);
    }

    @Override
    public Manager findById(int id) {
        return managerMapper.findById(id);
    }

    @Override
    public List<Manager> findAll() {
        return managerMapper.findAll();
    }
}
