package com.oaec.tourism.controller.admin.theme;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oaec.tourism.entity.Theme;
import com.oaec.tourism.service.ThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*主题Controller*/
@Controller
@RequestMapping("/admin/theme")
public class AdminThemeController {
    @Autowired
    private ThemeService themeService;
    /*查询所有*/
    @GetMapping("list")
    public String findAll(Model model,@RequestParam(name = "pageNum",defaultValue = "0")Integer pageNum){
        PageHelper.startPage(pageNum,8);//分页
        List<Theme> list = themeService.findAll(0);
        PageInfo<Theme> pageInfo = new PageInfo<>(list);
        model.addAttribute("themeList",pageInfo.getList());
        model.addAttribute("pages",pageInfo.getPages());//由页面获取页数
        model.addAttribute("this",pageInfo.getPageNum());//当前页
        return "/admin/theme/theme_list";
    }
    /*修改或新增*/
    @GetMapping("edit")
    public String  edit(@RequestParam(name = "id",required = false) Integer id , Model model){
        if(id!=null && !id.equals("")){//修改
            Theme theme = themeService.findById(id, 0);
            model.addAttribute("theme",theme);
        }//新增
        return "/admin/theme/theme_edit";
    }
    /*保存*/
    @PostMapping("save")
    public String save(@RequestParam(name = "id",required = false) Integer id,String name,String detail,RedirectAttributes attributes){
        if(id!=null && !id.equals("")){//修改
            Theme theme = new Theme();
            theme.setId(id);
            theme.setName(name);
            theme.setDetail(detail);
            int row = themeService.update(theme);
            if(row>0){
                attributes.addFlashAttribute("success","修改主题成功!");
            }else{
                attributes.addFlashAttribute("error","修改主题失败!");
            }
        }else{//新增
            if(name!=null && !name.equals("")){
                int row = themeService.createTheme(name, detail);
                if(row>0){
                    attributes.addFlashAttribute("success","新增主题成功!");
                }else{
                    attributes.addFlashAttribute("error","新增主题失败!");
                }
            }else{
                attributes.addFlashAttribute("error","主题名称不能为空!");
            }
        }
        return "redirect:/admin/theme/list";
    }
    /*关键字查询主题*/
    @RequestMapping("key")
    public String findAllKey(String key,Model model,@RequestParam(name = "pageNum",defaultValue = "0")Integer pageNum){
        PageHelper.startPage(pageNum,8);
        List<Theme> list = themeService.findAllKey(key, 0);
        PageInfo<Theme> pageInfo = new PageInfo<>(list);
        model.addAttribute("themeList",pageInfo.getList());
        model.addAttribute("pages",pageInfo.getPages());//总页数
        model.addAttribute("this",pageInfo.getPageNum());//当前页数
        model.addAttribute("key",key);
        if(list.size()==0){
            model.addAttribute("error","关键字‘"+key+"’搜索,暂无当前记录!");
        }else{
            model.addAttribute("success","关键字‘"+key+"’搜索,当前共有"+list.size()+"条记录数!");
        }
        return "/admin/theme/theme_list";
    }
    /*进入回收站操作*/
    @RequestMapping("recycle")
    public String recycleAll(String act,
                             @RequestParam(name = "key",required = false) String key,
                             @RequestParam(name = "id",required =false) Integer id, Model model,RedirectAttributes attributes){
        List<Theme> list =null;
        switch (act){
            case"list"://查询所有
                list = themeService.findAll(1);
                model.addAttribute("themeList",list);
                break;
            case"key"://关键字查询
                list = themeService.findAllKey(key, 1);
                if(list.size()==0){
                    model.addAttribute("error","关键字‘"+key+"’查找:暂无当前相关记录!");
                }else{
                    model.addAttribute("success","关键字‘"+key+"’查找:共有‘"+list.size()+"’条相关记录!");
                }
                model.addAttribute("themeList",list);
                break;
            case"delete"://清空所有或者删除单条
                if(id!=null  && !id.equals("")){//删除单条
                    int i = themeService.closeRecovery(id);
                    if(i>0){
                        attributes.addFlashAttribute("success","成功删除一条数据!");
                    }else{
                        attributes.addFlashAttribute("error","删除数据失败!");
                    }
                    return "redirect:/admin/theme/recycle?act=list";
                }else{//清空所有
                    int i = themeService.closeRecovery(null);
                    if(i>0){
                        model.addAttribute("success","成功清空回收站!");
                    }else{
                        model.addAttribute("error","清空回收站失败!");
                    }
                }
                break;
            case"return"://恢复至列表
                if(id!=null && !id.equals("") ){//恢复单条
                    int i = themeService.returnList(id);
                    if(i>0){
                        attributes.addFlashAttribute("success","成功恢复一条数据!");
                    }else{
                        attributes.addFlashAttribute("error","恢复数据失败!");
                    }
                    return "redirect:/admin/theme/recycle?act=list";
                }else{//恢复全部
                    int i = themeService.returnList(null);
                    if(i>0){
                        model.addAttribute("success","恢复所有数据成功!!");
                    }else{
                        model.addAttribute("error","数据恢复失败!");
                    }
                }
                break;
        }
        return "/admin/theme/recovery_list";
    }
    /*加入回收站*/
    @GetMapping("addRecycle")
    public String addRecycle(int id,RedirectAttributes attributes){
        int row = themeService.addRecovery(id);
        if( row >0){
            attributes.addFlashAttribute("success","成功加入回收站!如需恢复请至回收站恢复!");
        }else{
            attributes.addFlashAttribute("error","加入回收站失败!内部错误!");

        }
        return "redirect:/admin/theme/list";
    }

    /*Ajax请求查询是否存在当前名称*/
    @GetMapping("input")
    @ResponseBody
    public Map<String,Object> inputName(String name){
        Theme theme = themeService.findByName(name);
        Map<String,Object> map = new HashMap<>();
        map.put("ok",false);
        if(theme!=null){
            map.put("ok",true);
        }
        return map;
    }
}
