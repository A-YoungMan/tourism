package com.oaec.tourism.controller.admin.place;

import com.oaec.tourism.entity.Place;
import com.oaec.tourism.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/*景点回收站*/
@Controller
@RequestMapping("/admin/place")
public class PlaceRecycleController {
    @Autowired
    private PlaceService placeService;
    @GetMapping("recycleList")
    public String findAll(Model model){
        List<Place> all = placeService.findAll(1);//加入回收站的所有资源
        model.addAttribute("placeList",all);
        return "/admin/place/recycle_list";
    }
    /*删除*/
    @GetMapping("recycleDelete")
    public String deletet(@RequestParam(name = "id") Integer id, RedirectAttributes attributes){
        int i = placeService.emptyAll(id);
        if(i>0){
            attributes.addFlashAttribute("success","删除成功!");
        }else{
            attributes.addFlashAttribute("error","删除失败!");
        }
        return "redirect:/admin/place/recycleList";
    };
    /*恢复*/
    @GetMapping("recycleUpdate")
    public String update(@RequestParam(name = "id") Integer  id, RedirectAttributes attributes){
        int i = placeService.updateAll(id);
        if(i>0){
            attributes.addFlashAttribute("success","恢复成功!");
        }else{
            attributes.addFlashAttribute("error","恢复失败!");
        }
        return "redirect:/admin/place/recycleList";
    }
    /*清空所有或恢复所有*/
    @GetMapping("operation")
    public String operation(String act,RedirectAttributes attributes){
        switch (act){
            case "delete":
                int i = placeService.emptyAll(null);
                if(i>0){
                    attributes.addFlashAttribute("success","清空回收站成功!");
                }else{
                    attributes.addFlashAttribute("error","清空失败!");
                }
                break;
            case"update":
                int row = placeService.updateAll(null);
                if(row>0){
                    attributes.addFlashAttribute("success","恢复所有成功!");
                }else{
                    attributes.addFlashAttribute("error","恢复失败!");
                }
                break;
        }
        return "redirect:/admin/place/recycleList";
    }

}
