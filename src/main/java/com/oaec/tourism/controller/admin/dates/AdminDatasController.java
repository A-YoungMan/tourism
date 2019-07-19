package com.oaec.tourism.controller.admin.dates;

import com.oaec.tourism.entity.Order;
import com.oaec.tourism.entity.Place;
import com.oaec.tourism.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * 数据显示与订单管理管理
 */
@Controller
@RequestMapping(value = "/admin/order")
public class AdminDatasController {
    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "list",method = RequestMethod.GET)
    public String findAll(){
        return "/admin/order/order_list";
    }


    @RequestMapping(value = "ajax",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> ajaxFindDatas(String act,
                                            @RequestParam(value = "date",required = false) String date){
        Map<String,Object> map = new HashMap<>();
        map.clear();
        List<Order> orders =null;
        switch (act){
            case "list"://页面加载请求操作数据
                orders= orderService.findAll();//查询已经存在的订单
                map = putPlaceNameOrPrice(orders);
                break;
            case"time": //选择时间查询
                orders = orderService.findByCreateTime(date);
                map = putPlaceNameOrPrice(orders);
                break;
        }

        return map;
    }
    //获取名称已总额
    public  Map<String,Object> putPlaceNameOrPrice(List<Order> orders){
        Map<String,Object> map = new HashMap<>();
        Set<Place> setPlace =null;
        if(orders != null){
            setPlace = new HashSet<>();//获取景区
            for(Order order:orders){
                setPlace.add(order.getTrip().getPlace());
            }
        }
        if(setPlace !=null){
            String [] placeName =new String[setPlace.size()];
            Double [] price = new Double[setPlace.size()];
            int index =0;
            for(Place place: setPlace){
                placeName[index] = place.getName();
                List<Order> orders1 = orderService.findByPlaceId(place.getId());
                price[index] =0.0;
                for(Order order: orders1){
                    price[index] +=order.getTotalPrice();
                }
                index++;
            }
            map.put("placeName",placeName);
            map.put("price",price);
        }
        return map;
    }
}
