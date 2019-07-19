package com.oaec.tourism.controller.admin.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/test")
public class TestController {
    @GetMapping("list")
    public String test(){
        return "/admin/test";
    }
}
