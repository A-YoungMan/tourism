package com.oaec.tourism.controller.admin.user;

import com.oaec.tourism.entity.Manager;
import com.oaec.tourism.service.ManagerService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

/*管理员你操作管理员Controller*/
@Controller
@RequestMapping("/admin/manager")
public class AdminManagerController {
    @Autowired
    private ManagerService managerService;

    /*查询所有管理员*/
    @GetMapping("list")
    public String findAllAdmin(Model model) {
        List<Manager> all = managerService.findAll();
        model.addAttribute("managerList", all);
        return "/admin/admin/admin_list";
    }

    /*详情*/
    @GetMapping("details")
    public String details(int id, Model model) {
        Manager manager = managerService.findById(id);
        model.addAttribute("manager", manager);
        return "/admin/admin/admin_details";
    }

    /*修改或新增*/
    @GetMapping("edit")
    public String edit(@RequestParam(value = "id", required = false) Integer id, Model model) {
        if (id != null && !id.equals("")) {//修改
            Manager manager = managerService.findById(id);
            model.addAttribute("manager", manager);
        }
        return "/admin/admin/admin_edit";
    }

    /*保存*/
    @PostMapping("save")
    public String save(Manager manager, RedirectAttributes attributes) {
        if (manager.getId() != null && !manager.getId().equals("")) {//修改
            int i = managerService.updateAdmin(manager);
            if(i>0){
                attributes.addFlashAttribute("success","修改管理员成功!");
            }else{
                attributes.addFlashAttribute("error","修改管理员失败!");
            }
        } else {
            Map<String, Object> map = managerService.addAdmin(manager);
            if((boolean)map.get("ok")){
                attributes.addFlashAttribute("success","新增管理员成功!");
            }else{
                attributes.addFlashAttribute("error","新增管理员失败!");
            }
        }
        return "redirect:/admin/manager/list";
    }

}
