package com.oaec.tourism.controller.error;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
/*权限异常Controller*/
@Controller
@RequestMapping("/error")
public class ShiroErrorController {
    @GetMapping("403")
    public String handleError(HttpServletRequest request){
        return "/error/403";
    }
}
