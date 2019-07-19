package com.oaec.tourism.shiro;

import com.oaec.tourism.entity.Manager;
import com.oaec.tourism.entity.User;
import com.oaec.tourism.service.ManagerService;
import com.oaec.tourism.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class MyShiroRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;
    @Autowired
    private ManagerService managerService;

    /*认证*/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("Shiro认证··············");
        //获取令牌
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        //从令牌中获取用户名
        String username = token.getUsername();
        User user = null; //普通用户
        Manager manager = null;//管理员
        //按用户名查询用户
        /*正则表达式匹配是否是手机号码*/
        boolean matches = username.matches("^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$");
        if(matches){
            //捕获，不输出异常
            try {
                user = userService.findByUserPhone(username);
            }catch (Exception e){}
        }else{
            try {
                manager = managerService.findByAccount(username);
            } catch (Exception e) {
            }
        }
        if (user != null) {//用户登录
            //获取用户的盐
            String salt = user.getSalt();
            ByteSource saltBytes = ByteSource.Util.bytes(salt);
            //创建简单的认证信息对象
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(user,user.getPassword(), saltBytes, getName());
            return simpleAuthenticationInfo;
        }else if(manager !=null){//管理员登录
            String salt = manager.getSalt();
            ByteSource saltBytes = ByteSource.Util.bytes(salt);
            //创建简单的认证信息对象
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo( manager, manager.getPassword(), saltBytes, getName());
            return simpleAuthenticationInfo;
        }
        return null;
    }

    /*授权*/
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("Shiro授权··············");
        //简单授权
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        User user = null;
        Manager manager = null;
        //捕获，不输出异常
        try {
            user = (User) principalCollection.getPrimaryPrincipal();
        }catch (Exception e){}
        try {
            manager = (Manager) principalCollection.getPrimaryPrincipal();
        } catch (Exception e) {
        }
        //构造权限
        List<String> list = new ArrayList<>();
        if (user != null) {//普通用户
            list.add("/user/**");
            simpleAuthorizationInfo.addRole("user");
            System.out.println("普通用户认证");
        }else if (manager != null) {//管理员
                list.add("/admin/**");
                list.add("/user/**");
                simpleAuthorizationInfo.addRole("admin");
                System.out.println("管理员认证");
        }
        simpleAuthorizationInfo.addStringPermissions(list);
        return simpleAuthorizationInfo;
    }
}
