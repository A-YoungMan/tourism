package com.oaec.tourism.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oaec.tourism.entity.User;
import com.oaec.tourism.mapper.UserMapper;
import com.oaec.tourism.service.UserService;
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
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String, Object> login(String phone, String password) {
        Map<String, Object> map = new HashMap<>();
        map.put("ok", false);
        boolean b = phone.matches("^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$");
        if (b) {
            //使用shiro的认证
            Subject subject = SecurityUtils.getSubject();
            //创建令牌
            UsernamePasswordToken token = new UsernamePasswordToken(phone, password);
            try {
                subject.login(token);
                map.put("ok", true);
                //从subject中获取当前登录用户的对象
                User user = (User) subject.getPrincipal();
                map.put("user", user);
            } catch (UnknownAccountException e) {
                map.put("error", "登录失败！用户不存在！");
            } catch (IncorrectCredentialsException e) {
                map.put("error", "登录失败！密码错误！");
            } catch (Exception e) {
                map.put("error", "登录失败！内部错误！");
            }
        } else {
            map.put("error", "请输入正确的手机号码!");
        }
        return map;
    }

    @Override
    public Map<String, Object> register(String phone, String password, String username, int sex, String email) {
        Map<String, Object> map = new HashMap<>();
        map.put("ok", false);
        if (phone != null && !phone.equals("")) { //判断是否是合法手机号
            boolean b = phone.matches("^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$");
            if (b) {
                if (password != null && !password.equals("")) {
                    if (email != null && !email.equals("")) { //判断是否是合法邮件
                        b = email.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
                        if (b) {
                            User user1 = userMapper.findByUserPhone(phone);
                            if (user1 == null) {
                                User user = new User();
                                user.setPhone(phone);
                                //获取盐
                                String salt = ShiroUtil.createSalt();
                                user.setSalt(salt);
                                password = ShiroUtil.createPwdBySalt(password, salt);
                                user.setPassword(password);
                                user.setUsername(username);
                                user.setSex(sex);
                                user.setEmail(email);
                                user.setCreateTime(new Date());
                                int i = userMapper.create(user);
                                if (i > 0) {
                                    map.put("ok", true);
                                }
                            } else {
                                map.put("error", "该用户已存在!");
                            }
                        } else {
                            map.put("error", "请输入正确的电子邮件!");
                        }
                    } else {
                        map.put("error", "邮件不能为空!");
                    }
                } else {
                    map.put("error", "，密码不能为空!");
                }
            } else {
                map.put("error", "请输入正确的手机号码!");
            }
        } else {
            map.put("error", "手机号码不能为空!");
        }
        return map;
    }

    @Override
    public Map<String,Object> create(User user) {
        Map<String,Object> map = new HashMap<>();
        map.put("ok",false);
        user.setCreateTime(new Date());
        String salt =ShiroUtil.createSalt();//获取盐
        String password =ShiroUtil.createPwdBySalt(user.getPassword(),salt);
        user.setSalt(salt);
        user.setPassword(password);
        int i = userMapper.create(user);
        if(i>0){
            map.put("ok",true);
        }
        return map;
    }

    @Override
    public List<User> findAll() {
        return userMapper.findAll();
    }

    @Override
    public User findById(int id) {
        return userMapper.findById(id);
    }

    @Override
    public User findByUserPhone(String phone) {
        return userMapper.findByUserPhone(phone);
    }

    @Override
    public User findByCommentUserId(int id) {
        return userMapper.findByCommentUserId(id);
    }

    @Override
    public int updateUserInfo(User user) {
        return userMapper.update(user);
    }
}
