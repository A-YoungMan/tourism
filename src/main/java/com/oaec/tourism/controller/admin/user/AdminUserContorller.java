package com.oaec.tourism.controller.admin.user;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oaec.tourism.entity.User;
import com.oaec.tourism.service.ManagerService;
import com.oaec.tourism.service.UserService;
import com.oaec.tourism.shiro.ShiroUtil;
import com.oaec.tourism.util.ContextPath;
import com.oaec.tourism.util.ImgUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.annotation.MultipartConfig;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/*管理员操作用户Controller*/
@Controller
@RequestMapping("/admin/user")
public class AdminUserContorller {
    @Autowired
    private UserService userService;
    /*查询所有普通用户*/
    @GetMapping("list")
    public String findUser(Model model,@RequestParam(name = "pageNum",defaultValue = "0") int pageNum){
        PageHelper.startPage(pageNum,10);//分页查询
        List<User> all = userService.findAll();
        PageInfo<User> pageInfo = new PageInfo<>(all);
        System.out.println(pageInfo.getPages());
        model.addAttribute("userList",pageInfo.getList());
        model.addAttribute("pages",pageInfo.getPages());//由页面获取页数
        model.addAttribute("this",pageInfo.getPageNum());//当前页
        return "/admin/user/user_list";
    }
    /*用户详情*/
    @GetMapping("details")
    public String details(int id,Model model){
        User user = userService.findById(id);
        model.addAttribute("user",user);
        return "/admin/user/user_details";
    }
    /*新增或修改*/
    @GetMapping("edit")
    public String edit(@RequestParam(value = "id",required = false) Integer id, Model model){
        if(id!=null&&!id.equals("")){//修改
            User user = userService.findById(id);
            model.addAttribute("user",user);
        }
        //新增
        return "/admin/user/user_edit";
    }
    /*保存*/
    @PostMapping("save")
    public String save(@Valid User user, BindingResult result, RedirectAttributes attributes,@RequestParam(value = "upload-file",required = false) MultipartFile file){
        if(result.hasErrors()){
           List<ObjectError> allErrors = result.getAllErrors();
           for(ObjectError error : allErrors){
               FieldError fieldError = (FieldError) error;
               String name = fieldError.getField();//属性
               String message = fieldError.getDefaultMessage();//提示
               System.out.println(name+":"+message);
               attributes.addFlashAttribute("error",message);
           }
           return "redirect:/admin/user/list";
       }else{
            /*文件上传*/
            if(!file.isEmpty()){
                Map<String, Object> upload = ImgUploadUtil.imgUpload(ContextPath.ABC_USER_IMG_UPLOAD, ContextPath.REL_USER_IMG_PATH, file);
                if((boolean)upload.get("ok")){
                    System.out.println("文件上传成功!");
                    System.out.println(upload.get("path"));
                    user.setImgPath((String)upload.get("path"));
                }else{
                    attributes.addFlashAttribute("error",upload.get("error"));
                }
            }
           if(user.getId()!=null&&!user.getId().equals("")){//修改
               int i = userService.updateUserInfo(user);
               if(i>0){
                   attributes.addFlashAttribute("success","修改用户成功!");
                   return "redirect:/admin/user/list";
               }else{
                   attributes.addFlashAttribute("error","修改用户失败!");
                   return "redirect:/admin/user/list";
               }
           }else{//新增

               Map<String, Object> map = userService.create(user);
               if((boolean)map.get("ok")){
                   attributes.addFlashAttribute("success","添加用户成功!");
                   return "redirect:/admin/user/list";
               }else{
                   attributes.addFlashAttribute("error","添加用户失败!");
                   return "redirect:/admin/user/list";
               }
           }
       }

    }
}
