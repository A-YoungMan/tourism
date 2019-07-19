package com.oaec.tourism.controller.admin.place;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oaec.tourism.entity.City;
import com.oaec.tourism.entity.Place;
import com.oaec.tourism.service.CityService;
import com.oaec.tourism.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/place")
public class AdminPlaceController {
    @Autowired
    private PlaceService placeService;
    @Autowired
    private CityService cityService;

    /*景点列表*/
    @GetMapping("list")
    public String AllPlace(Model model,@RequestParam(name = "pageNum",defaultValue = "0") int pageNum) {
        PageHelper.startPage(pageNum,10);
        List<Place> list = placeService.findAll(0);
        PageInfo<Place> pageInfo = new PageInfo<>(list);
        model.addAttribute("placeList", pageInfo.getList());
        model.addAttribute("pages",pageInfo.getPages());//由页面获取页数
        model.addAttribute("this",pageInfo.getPageNum());//当前页
        return "/admin/place/place_list";
    }

    /*Ajax对City城市表操作*/
    @RequestMapping("ajax")
    @ResponseBody
    public Map<String,Object> findAllCity(@RequestParam(name = "act", required = false) String act,
                                  @RequestParam(name = "id", required = false) Integer id,
                                  @RequestParam(name = "name", required = false) String name) {
        List<City> cityList = null;
        Map<String,Object> map = new HashMap<>();
        switch (act) {
            case"list"://查询所有
                cityList =cityService.findByType(0);
                map.put("list",cityList);
                break;
            case "add"://新增
                int i = cityService.create(name, 0, null);
                if(i>0){
                    map.put("type","ok");
                }else{
                    map.put("type","error");
                }
                break;
            case "update"://修改
                City city = cityService.findById(id);
                cityList = new ArrayList<>();
                cityList.set(0,city);
                break;
            case "delete"://删除
                 i = cityService.delete(id);
                break;
        }

        return map;
    }

    /*添加或修改*/
    @GetMapping("edit")
    public String addPlace(@RequestParam(name = "id", required = false) Integer id, Model model) {
        if (id != null && !id.equals("")) {//修改
            Place place = placeService.findById(id);
            model.addAttribute("place", place);
        }//新增
        return "/admin/place/place_edit";
    }

    /*保存*/
    @PostMapping("save")
    public String save(Place place, Float p1, Float p2, Integer cityId, RedirectAttributes attributes) {
        if (place.getId() != null && !place.equals("")) {
            int row = placeService.update(place, p1, p2,cityId);
            if (row > 0) {
                attributes.addFlashAttribute("success", "修改景点属性成功!");
            } else {
                attributes.addFlashAttribute("error", "修改景点属性失败!");
            }
        } else {
            int row = placeService.addPlace(place.getName(), place.getArea(), p1, p2, cityId);
            if (row > 0) {
                attributes.addFlashAttribute("success", "新增景点成功!");
            } else {
                attributes.addFlashAttribute("error", "新增景点失败!");
            }
        }
        return "redirect:/admin/place/list";
    }

    /*删除景点*/
    @GetMapping("delete")
    public String delete(int id, RedirectAttributes attributes) {
        int row = placeService.delete(id);
        if (row > 0) {
            attributes.addFlashAttribute("success", "景点删除成功!,如需恢复前往回收站!");
        } else {
            attributes.addFlashAttribute("error", "景点删除失败!");
        }
        return "redirect:/admin/place/list";
    }

    /*按关键字查询*/
    @PostMapping("key")
    public String strKey(@RequestParam(name = "type", required = false) Integer type, String key, Model model,@RequestParam(name = "pageNum",defaultValue = "0") int pageNum) {
        PageHelper.startPage(pageNum,10);
        List<Place> placeList = null;
        String name = null;
        switch (type) {
            case 0: //按国家查找
                placeList = placeService.findAllCityNameAndType(type, key);
                name = "国家";
                break;
            case 1: //按省级查找
                placeList = placeService.findAllCityNameAndType(type, key);
                name = "省级";
                break;
            case 2: //按城市查找
                placeList = placeService.findAllCityNameAndType(type, key);
                name = "城市";
                break;
            case 3: //按县级查找
                placeList = placeService.findAllCityNameAndType(type, key);
                name = "县级";
                break;
            default: //按景点关键查找
                placeList = placeService.findAllName(0, key);
                name = "景点";
                break;
        }
        if (placeList.size() == 0) {
            model.addAttribute("error", "暂无记录!‘" + name + "’查找-->>关键字‘" + key + "’:暂无相关记录!");
        } else {
            model.addAttribute("success", "搜索成功!‘" + name + "’查找-->>关键字‘" + key + "’:为您查找到" + placeList.size() + "条记录!");
        }
        PageInfo<Place> pageInfo = new PageInfo<>(placeList);
        model.addAttribute("placeList", pageInfo.getList());
        model.addAttribute("pages",pageInfo.getPages());//由页面获取页数
        model.addAttribute("this",pageInfo.getPageNum());//当前页
        return "/admin/place/place_list";
    }
}
