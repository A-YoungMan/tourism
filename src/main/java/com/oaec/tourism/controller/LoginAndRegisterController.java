package com.oaec.tourism.controller;

import com.oaec.tourism.entity.User;
import com.oaec.tourism.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 用户登录与注册
 */
@Controller
public class LoginAndRegisterController {
    @Autowired
    private UserService userService;

    @GetMapping("login")
    public String login(){
        return "login";
    }
    @PostMapping("login")
    public String login(String phone, String password, Model model, HttpSession session){
            Map<String, Object> map = userService.login(phone, password);
            if((boolean) map.get("ok")){
                Subject subject = SecurityUtils.getSubject();
                User user = (User) subject.getPrincipal();//存入session用于判断用户是否登录
                session.setAttribute("userLogin",user);
                return "redirect:index";
            }else{
                model.addAttribute("error",map.get("error"));
                return "login";
            }
    }

    @GetMapping("register")
    public String register(){
        return "register";
    }
    @PostMapping("register")
    public String register(String phone, String username, String password, int sex, String email, RedirectAttributes attributes){
        Map<String, Object> map = userService.register(phone,password, username, sex, email);
        if((Boolean) map.get("ok")){
            attributes.addFlashAttribute("success","恭喜您,注册成功!");
            return "redirect:login";
        }else{
            attributes.addFlashAttribute("error",map.get("error"));
            return "redirect:register";
        }
    }

}
