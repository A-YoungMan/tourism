package com.oaec.tourism.controller.admin.trip;

import com.oaec.tourism.entity.Dates;
import com.oaec.tourism.service.DatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 行程操作
 */
@Controller
@RequestMapping("/admin/trip/dates")
public class AdminTripDatesController {
    @Autowired
    private DatesService datesService;

    /**
     * AJax请求往返行程简要概述操作
     * @param act //操作方式
     * @param tripId //景区id
     * @param id // 行程id
     * @param meal // 餐饮
     * @param hotel //酒店
     * @return
     */
    @RequestMapping(value = "ajax",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> TrafficEdit(String act,
                                          @RequestParam(value = "tripId",required = false)Integer tripId,
                                          @RequestParam(value = "id",required = false)Integer id,
                                          @RequestParam(value = "meal",required = false)String meal,
                                          @RequestParam(value = "hotel",required = false)String hotel,
                                          @RequestParam(value = "title",required = false)String title,
                                          @RequestParam(value = "detail",required = false)String detail,
                                          @RequestParam(value = "traffic",required = false)String traffic,
                                          @RequestParam(value = "prompt",required = false)String prompt){
        Map<String,Object> map = new HashMap<>();
        switch (act){
            case"query":
                List<Dates> datesList = datesService.findByTripId(tripId);
                map.put("list",datesList);
                break;
            case"update":
                Dates dates = new Dates();
                dates.setId(id);
                dates.setTitle(title);
                dates.setMeal(meal);
                dates.setHotel(hotel);
                dates.setTraffic(traffic);
                dates.setDetail(detail);
                dates.setPrompt(prompt);
                int row = datesService.update(dates);
                if(row>0){
                    map.put("ok",true);
                }
                break;
        }
        return map;
    }
}
