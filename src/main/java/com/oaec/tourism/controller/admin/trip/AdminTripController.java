package com.oaec.tourism.controller.admin.trip;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oaec.tourism.entity.Place;
import com.oaec.tourism.entity.Theme;
import com.oaec.tourism.entity.Trip;
import com.oaec.tourism.service.PlaceService;
import com.oaec.tourism.service.ThemeService;
import com.oaec.tourism.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/trip")
public class AdminTripController {
    @Autowired
    private PlaceService placeService;
    @Autowired
    private TripService tripService;
    @Autowired
    private ThemeService themeService;

    /**
     * 查询所有项目列表请求
     * @param model
     * @param pageNum
     * @return
     */
    @GetMapping("list")
    public String tripAll(Model model, @RequestParam(name = "pageNum", defaultValue = "0") Integer pageNum) {
        PageHelper.startPage(pageNum, 10);
        List<Place> all = placeService.findAll(0);
        PageInfo<Place> pageInfo = new PageInfo<>(all);
        model.addAttribute("placeList", pageInfo.getList());
        model.addAttribute("pages", pageInfo.getPages());//由页面获取页数
        model.addAttribute("this", pageInfo.getPageNum());//当前页
        return "admin/trip/trip_list";
    }

    /**
     * 未上架项目列表请求
     * @param model
     * @param pageNum
     * @return
     */
    @RequestMapping(value = "notShelf",method = RequestMethod.GET)
    public String findAllNotShelf(Model model, @RequestParam(name = "pageNum", defaultValue = "0") Integer pageNum){
        PageHelper.startPage(pageNum, 10);
        List<Trip> all = tripService.findAll(0, 1);
        PageInfo<Trip> pageInfo = new PageInfo<Trip>(all);
        model.addAttribute("tripList",pageInfo.getList());
        model.addAttribute("pages", pageInfo.getPages());//由页面获取页数
        model.addAttribute("this", pageInfo.getPageNum());//当前页
        return "admin/trip/notShelf_list";
    }

    /**
     * 对为上架列表项目进行上架或删除操作
     * @parma act //操作类型
     * @param id
     * @param attributes 重定向带参数
     * @return
     */
    @RequestMapping(value = "notShelf/edit",method = RequestMethod.GET)
    public String notShelfEdit(String act,Integer id,RedirectAttributes attributes){
        switch (act){
            case"return": //修改
                int i = tripService.updateIsOk(id, 0);
                if(i>0){
                    attributes.addFlashAttribute("success","成功上架一条数据!~");
                }else{
                    attributes.addFlashAttribute("error","上架失败");
                }
                break;
            case"delete": //删除
                int row = tripService.closeRecovery(id);
                if(row>0){
                    attributes.addFlashAttribute("success","删除一条数据成功!~");
                }else{
                    attributes.addFlashAttribute("error","删除失败");
                }
                break;
        }
        return "redirect:/admin/trip/notShelf";
    }

    /**
     *  回收站列表
     * @param model
     * @param pageNum
     * @return
     */
    @GetMapping("recycle")
    public String recycle(Model model, @RequestParam(name = "pageNum", defaultValue = "0") Integer pageNum) {
        PageHelper.startPage(pageNum, 10);//分页
        List<Trip> list = tripService.findAll(1, null);
        PageInfo<Trip> pageInfo = new PageInfo<>(list);
        model.addAttribute("tripList", pageInfo.getList());
        model.addAttribute("pages", pageInfo.getPages());//由页面获取页数
        model.addAttribute("this", pageInfo.getPageNum());//当前页
        return "admin/trip/tripRecycle_list";
    }

    /*操作回收站*/
    @GetMapping("recycleEdit")
    public String recycleEdit(String act, @RequestParam(name = "id", required = false) Integer id, RedirectAttributes attributes) {
        int i = 0;
        switch (act) {
            case "recover": //恢复单条数据
                i = tripService.returnList(id);
                if (i > 0) {
                    attributes.addFlashAttribute("success", "成功恢复一条数据!");
                } else {
                    attributes.addFlashAttribute("error", "恢复数据失败!");
                }
                break;
            case "delete": //删除单条数据
                i = tripService.closeRecovery(id);
                if (i > 0) {
                    attributes.addFlashAttribute("success", "成功删除一条数据!");
                } else {
                    attributes.addFlashAttribute("error", "删除数据失败!");
                }
                break;
            case "allRecover"://恢复回收站的至列表
                i = tripService.returnList(null);
                if (i > 0) {
                    attributes.addFlashAttribute("success", "恢复所有数据成功!");
                } else {
                    attributes.addFlashAttribute("error", "恢复所有数据失败!");
                }
                break;
            case "allDelete"://清空回收站
                i = tripService.closeRecovery(null);
                if (i > 0) {
                    attributes.addFlashAttribute("success", "清空回收站成功!");
                } else {
                    attributes.addFlashAttribute("error", "清空回收站失败!");
                }
                break;
        }
        return "redirect:/admin/trip/recycle";
    }

    /*页面修改或新增旅游项目*/
    @GetMapping("edit")
    public String edit(@RequestParam(name = "id", required = false) Integer id, Model model) {
        if (id != null && !id.equals("")) {
            Trip trip = tripService.findByIdAndNum(id, null, null);
            model.addAttribute("trip", trip);
        } else { //新增
            List<Place> all = placeService.findAll(0);
          model.addAttribute("placeList", all);
        }
        return "admin/trip/trip_edit";
    }

    /*新增或修改*/
    @PostMapping("save")
    public String save(Trip trip, @RequestParam(name = "palceName", required = false) String palceName,
                       Integer placeId, Integer province, Integer cityId, Integer area,
                       RedirectAttributes attributes) {
        if (trip.getId() != null && !trip.getId().equals("")) {//修改
            int row = tripService.update(trip, province, cityId, area);
            if (row > 0) {
                String tour = "";
                if (trip.getType() == 0) {
                    tour = "自驾游";
                } else if (trip.getType() == 1) {
                    tour = "国内游";
                } else {
                    tour = "境外游";
                }
                attributes.addFlashAttribute("success", "修改成功!景点名称‘" + palceName + "’,出游类型‘" + tour + "’");
            } else {
                attributes.addFlashAttribute("error", "修改失败!");
            }
        } else { //新增
            Map<String, Object> map = tripService.addTrip(trip, placeId, province, cityId, area);
            if ((boolean) map.get("ok")) {
                attributes.addFlashAttribute("success","新增景点成功!");
            } else {
                attributes.addFlashAttribute("error", map.get("error"));
            }
        }
        return "redirect:/admin/trip/list";
    }

    /*Ajax加入回收站删除恢复功能 //上下架功能// 修改标题，小标题，出行天数*/
    @GetMapping("recovery")
    @ResponseBody
    public Map<String, Object> recovery(@RequestParam(name = "id", required = false) Integer id, String act,
                                        @RequestParam(name = "title", required = false) String title,
                                        @RequestParam(name = "time", required = false) Integer time,
                                        @RequestParam(name = "sTitle", required = false) String sTitle) {//使用act座位判断是否删或清空
        Map<String, Object> map = new HashMap<>();
        int row = 0;
        switch (act) {
            case "title"://修改标题或日期
                Trip trip = new Trip();
                trip.setId(id);
                if (title != null && !title.equals("")) {
                    trip.setTitle(title);
                }
                if (sTitle != null && !sTitle.equals("")) {
                    trip.setSTitle(sTitle);
                }
                if (time != null && !time.equals("")) {
                    trip.setTime(time);
                }
                row = tripService.update(trip, null, null, null);
                if (row > 0) {
                    map.put("success", "修改数据成功!");
                } else {
                    map.put("error", "修改失败!");
                }
                break;
            case "up"://上架
                row = tripService.updateIsOk(id, 0);
                if (row > 0) {
                    map.put("success", "上架一条数据成功!");
                } else {
                    map.put("error", "上架失败!");
                }
                break;
            case "down"://下架
                row = tripService.updateIsOk(id, 1);
                if (row > 0) {
                    map.put("success", "成功下架一条数据!");
                } else {
                    map.put("error", "下架失败!");
                }
                break;
            case "add"://加入回收站
                row = tripService.addRecovery(id);
                if (row > 0) {
                    map.put("success", "加入回收站!");
                } else {
                    map.put("error", "加入失败!");
                }
                break;
            case "delete"://删除单条
                row = tripService.closeRecovery(id);
                if (row > 0) {
                    map.put("success", "已成功删除一条数据!");
                } else {
                    map.put("error", "删除失败!");
                }
                break;
            case "close"://清空回收站
                row = tripService.closeRecovery(null);
                if (row > 0) {
                    map.put("success", "清空回收站成功!");
                } else {
                    map.put("error", "清空失败!");
                }
                break;
            case "return"://恢复单条数据
                row = tripService.returnList(id);
                if (row > 0) {
                    map.put("success", "成功恢复一条数据!");
                } else {
                    map.put("error", "恢复失败!");
                }
                break;
            case "returnList"://恢复所有
                row = tripService.returnList(null);
                if (row > 0) {
                    map.put("success", "已恢复成功!");
                } else {
                    map.put("error", "恢复失败!");
                }
                break;
        }
        return map;
    }

    /*Ajax新增项目保存*/
    @PostMapping("addtrip")
    @ResponseBody
    public Map<String, Object> save(Trip trip, Integer placeId, String province, String name, String area) {//字符串为省，市，县级
        Map<String, Object> map = tripService.addAjaxTrip(trip, placeId, province, name, area);
        if ((Boolean) map.get("ok")) {
            map.put("ok", "ok");
        } else {
            map.put("error", map.get("error"));
        }
        return map;
    }

    /*Ajax请求获取景区*/
    @RequestMapping("ajax")
    @ResponseBody
    public Map<String, Object> findAllAjaxAndType(@RequestParam(name = "type") int type, @RequestParam(name = "name") String name) {
        List<Trip> list = tripService.findAllPlaceByName(name.trim(), null, type);
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        return map;
    }

    /*按关键字查询*/
    @RequestMapping("key")
    public String strKey(@RequestParam(name = "type", required = false) Integer type, String key, Model model, @RequestParam(name = "pageNum", defaultValue = "0") Integer pageNum) {
        PageHelper.startPage(pageNum, 10);
        List<Place> placeList = null;
        String name = null;
        switch (type) {
            case 0: //按国家查找
                placeList = placeService.findAllCityNameAndType(type, key);
                name = "国家";
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
        model.addAttribute("pages", pageInfo.getPages());//由页面获取页数
        model.addAttribute("this", pageInfo.getPageNum());//当前页
        model.addAttribute("key", key);
        model.addAttribute("type", type);
        return "/admin/trip/trip_list";
    }

    /*旅游主题操作*/
    @RequestMapping("theme")
    @ResponseBody
    public Map<String, Object> themeEdit(String act,
                                         @RequestParam(name = "tripId", required = false) Integer tripId,
                                         @RequestParam(name = "themeId", required = false) Integer themeId) {
        Map<String, Object> map = new HashMap<>();
        List<Theme> list = null;
        switch (act) {
            case "list":// 查询所有
                list = themeService.findAll(0);
                //判断当前景点是否存在主题//
                if (tripId != null && !tripId.equals("")) {
                    List<Theme> themeList = themeService.findByThemeIdOrTripId(null, tripId);
                    if (themeList.size() == 0) {
                        map.put("list", list);
                    } else {
                        List<Theme> listTheme = new ArrayList<>();
                        for (int i = 0; i < list.size(); i++) {
                            for (int j = 0; j < themeList.size(); j++) {
                                if (list.get(i).getId() == themeList.get(j).getId()) {
                                    listTheme.add(list.get(i));
                                    break;
                                }
                            }
                        }
                        list.removeAll(listTheme);//删除已存在的
                        map.put("list", list);
                    }
                } else {
                    map.put("list", list);
                }
                break;
            case "query"://查询单个
                list = themeService.findByThemeIdOrTripId(null, tripId);
                map.put("list", list);
                break;
            case "add"://添加
                Map<String, Integer> createMap = new HashMap<>();
                createMap.put("tripId", tripId);
                createMap.put("themeId", themeId);
                int row = themeService.createThemeOrTrip(createMap);
                if (row > 0) {
                    list = themeService.findByThemeIdOrTripId(null, tripId);
                }
                map.put("list", list);
                break;
            case "delete":// 修改
                Map<String, Integer> deleteMap = new HashMap<>();
                deleteMap.put("tripId", tripId);
                deleteMap.put("themeId", themeId);
                int i = themeService.deleteThemeOrTrip(deleteMap);
                if (i > 0) {
                    list = themeService.findByThemeIdOrTripId(null, tripId);
                }
                map.put("list", list);
                break;

        }
        return map;
    }

    /**
     * AJax按id查询 //查询缩进主题
     * @param id
     * @return
     */
    @RequestMapping(value = "ajax/query",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> queryTrip(Integer id,Integer type){
        Map<String,Object> map = new HashMap<>();
        Trip trip = tripService.findById(id);
        if(trip !=null){
            if(type == 0){//判断类型 0标题
                map.put("title",trip.getTitle());
            }else{ //1小标题
                map.put("stitle",trip.getSTitle());
            }
        }
        return map;
    }
}
