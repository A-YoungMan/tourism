package com.oaec.tourism.controller.ajax;
import com.oaec.tourism.entity.Region;
import com.oaec.tourism.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

/*user 异步请求操作
* 省市级联动*/
@Controller
@RequestMapping("/ajax")
public class AjaxController {
    @Autowired
    private RegionService regionService;
    /*省级联动*/
    @RequestMapping("region")
    @ResponseBody
    public List<Region> region(@RequestParam(value = "pid",required = false) String pid){
        List<Region> provinces =null;
        if(pid!=null&&!pid.equals("")){
            provinces = regionService.findByParentId(Integer.parseInt(pid));
        }else{
            provinces= regionService.findProvinces();
        }
       return provinces;
    }
}
