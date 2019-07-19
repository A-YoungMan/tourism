package com.oaec.tourism.controller.admin;

import com.oaec.tourism.entity.Manager;
import com.oaec.tourism.service.ManagerService;
import com.oaec.tourism.shiro.MyShiroRealm;
import org.apache.catalina.security.SecurityUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.Map;
/*管理员登录*/
@Controller
public class AdminLoginController {
    @Autowired
    private ManagerService managerService;
    @GetMapping("adminlogin")
    public String AdminLogin(){
        return "admin_login";
    }
    /*登录*/
    @PostMapping("adminlogin")
    public String AdminLogin(String account, String password, Model model){
        if(account!=null&&!account.equals("")){
            if(password!=null&&!password.equals("")){
                Map<String, Object> map = managerService.loginAdmin(account, password);
                if((boolean)map.get("ok")){
                    Subject subject = SecurityUtils.getSubject();
                    Manager manager =(Manager) subject.getPrincipal();
                    model.addAttribute("manager",manager);
                    return "/admin_index";
                }else{
                    model.addAttribute("error",map.get("error"));
                    return "admin_login";
                }
            }else{
                model.addAttribute("error","密码不能为空!");
                return "admin_login";
            }
        }else{
            model.addAttribute("error","账号不能为空!");
            return "admin_login";
        }
    }
    /*退出登录*/
    @GetMapping("adminOut")
    public String AdminOut(){
        //登出清除缓存
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        MyShiroRealm realm  = new MyShiroRealm();
        AuthorizationInfo admin = realm.getAuthorizationCache().remove("admin");
        subject.releaseRunAs();
        return "admin_login";
    }
}
