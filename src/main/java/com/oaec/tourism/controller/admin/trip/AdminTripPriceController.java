package com.oaec.tourism.controller.admin.trip;

import com.oaec.tourism.entity.Price;
import com.oaec.tourism.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*景区每日价格 全Ajax实现*/
@Controller
@RequestMapping("/admin/trip")
public class AdminTripPriceController {
    @Autowired
    private PriceService priceService;
    /*ajax请求操作*/
    @RequestMapping("price")
    @ResponseBody
    public Map<String,Object> priceAjaxEdit(String act,
                                            @RequestParam(name = "tripId",required = false) Integer tripId,
                                            @RequestParam(name = "priceId",required = false) Integer priceId,
                                            @RequestParam(name = "year",required = false) String year,
                                            @RequestParam(name = "month",required = false) String month,
                                            @RequestParam(name = "price",required = false) Float price){
        List<Price> list =null;
        Map<String,Object> map = new HashMap<>();
        switch(act){
            case "list"://查询当前trip的所有
                list = priceService.findByAllTripIdAndYearAndMonth(tripId,year,month);
                break;
            case"update"://修改
                int row = priceService.update(priceId, price);
                if(row>0){
                    map.put("ok",true);
                }
                break;
            case"delete"://删除
                 row = priceService.delete(priceId);
                if(row>0){
                    map.put("ok",true);
                }
                break;
        }
        map.put("list",list);
        return map;
    }
    /*单个添加*/
    @RequestMapping("price/add")
    @ResponseBody
    public Map<String,Object> add(Integer tripId,Float price ,String date){
        Map<String, Object> map = priceService.createPrice(tripId, date, price);
        return map;
    }
    /*多个添加*/
    @RequestMapping("price/adds")
    @ResponseBody
    public Map<String,Object> adds(Integer tripId,Float price ,String date){
        Map<String, Object> map = priceService.createPrice(tripId, date, price);
        return map;
    }

    /**
     * 按时间日期删除
     * @param tripId
     * @param year
     * @param month
     * @return
     */
    @RequestMapping(value = "price/deleteDate")
    @ResponseBody
    public Map<String,Object> deleteDate(@RequestParam(name = "tripId") Integer tripId,
                                         @RequestParam(name = "year") Integer year,
                                         @RequestParam(name = "month") Integer month){
        Map<String,Object> map = new HashMap<>();
        map.put("ok",false);
        int row = priceService.deleteDate(year, month, tripId);
        if(row>0){
            map.put("ok",true);
        }
        return map;
    }
}
