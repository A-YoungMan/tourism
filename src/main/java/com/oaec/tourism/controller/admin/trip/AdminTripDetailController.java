package com.oaec.tourism.controller.admin.trip;

import com.oaec.tourism.entity.Detail;
import com.oaec.tourism.service.DetailService;
import com.oaec.tourism.util.TxtPathUrlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * 增加旅游产品说明*/
@Controller
@RequestMapping("/admin/trip")
public class AdminTripDetailController {
    @Autowired
    private DetailService detailService;

    /*页面请求查询所有*/
    @RequestMapping("detailList")
    public Map<String, Object> findDetail(Model model) {
        List<Detail> detailList = detailService.findByAllOrPlaceId(null);
        model.addAttribute("detailList", detailList);
        return null;
    }

    /*Ajax对产品说明增删改查*/
    @RequestMapping("detail")
    @ResponseBody
    public Map<String, Object> editDetail(@RequestParam(name = "act")String act,
                                          @RequestParam(name = "id" ,required = false) Integer id,
                                          @RequestParam(name = "tripId", required = false) Integer tripId,
                                          @RequestParam(name = "place", required = false) String place,
                                          @RequestParam(name = "hotel", required = false) String hotel,
                                          @RequestParam(name = "food", required = false) String food) {
        Detail detail =null;
        Map<String,Object> map  = new HashMap<>();
        switch (act) {
            case "query"://查询
                 detail = detailService.findByIdOrTrip(null, tripId,true);
                 if(detail != null){
                 }else{ //不存在则新增
                      map = detailService.addDetail(tripId, "暂无景区详情 点击修改", "暂无酒店详情 点击修改", "暂无美食详情 点击修改");
                     if((boolean) map.get("ok")){
                         detail= detailService.findByIdOrTrip(null,tripId,true);
                     }
                 }
                break;
            case "update"://修改
                detail = new Detail();
                detail.setId(id);
                detail.setPlace(place);
                System.out.println(place);
                detail.setHotel(hotel);
                detail.setFood(food);
                detailService.update(detail);
                detail =detailService.findByIdOrTrip(id,null,true);
                break;
        }
        map.put("detail",detail);
        return map;
    }
}
