package com.oaec.tourism.controller.admin.trip;

import com.oaec.tourism.entity.Traffic;
import com.oaec.tourism.service.TrafficService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 景点往返操作Traffic Controller
 */
@Controller
@RequestMapping(value = "/admin/trip")
public class AdminTripTrafficController {
    @Autowired
    private TrafficService trafficService;

    /**
     * 查询所有列表往返行程列表
     *
     * @return
     */
    @RequestMapping(value = "traffic", method = RequestMethod.GET)
    public String findAll(Model model) {
        List<Traffic> list = trafficService.findAll();
        model.addAttribute("trafficList", list);
        return "admin/trip/tripTraffic_list";
    }

    /**
     * 添加往返
     *
     * @param attributes //重定向带提示
     * @return
     */
    @RequestMapping(value = "traffic/add", method = RequestMethod.POST)
    public String AddTraffic(String goPoint, String goTime, String returnPoint, String returnTime, RedirectAttributes attributes) {
        int row = trafficService.createTraffic(goPoint, goTime, returnPoint, returnTime);
        if (row > 0) {
            attributes.addFlashAttribute("success", "添加往返成功!");
        } else {
            attributes.addFlashAttribute("error", "添加往返成功!");
        }
        return "redirect:/admin/trip/traffic";
    }

    /**
     * Ajax请求操作，删除更新，详情查看
     *
     * @param act
     * @param id
     * @param goPoint
     * @param goTime
     * @param returnPoint
     * @param returnTime
     * @return Map Json
     */
    @RequestMapping(value = "traffic/ajax", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> ajaxAll(String act, @RequestParam(value = "id") Integer id,
                                       @RequestParam(value = "goPoint", required = false) String goPoint,
                                       @RequestParam(value = "goTime", required = false) String goTime,
                                       @RequestParam(value = "returnPoint", required = false) String returnPoint,
                                       @RequestParam(value = "returnTime", required = false) String returnTime) {
        Map<String, Object> map = new HashMap<>();
        map.put("ok", false);
        int row = 0;
        Traffic traffic = null;
        switch (act) {
            case "query"://详情查询
                traffic = trafficService.findById(id);
                map.put("traffic", traffic);
                map.put("ok", true);
                break;
            case "update": //修改
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:hh:mm");
                try {
                    traffic = new Traffic();
                    traffic.setId(id);
                    if(goPoint !=null && !goPoint.equals("")){
                        traffic.setGoPoint(goPoint);
                    }
                    if(returnPoint !=null && !returnPoint.equals("")){
                        traffic.setReturnPoint(returnPoint);
                    }
                    if(goTime !=null &&!goTime.equals("")){
                        traffic.setGoTime(sdf.parse(goTime));
                    }
                    if(returnTime != null && !returnPoint.equals("")){
                        traffic.setReturnTime(sdf.parse(returnTime));
                    }
                    row = trafficService.update(traffic);
                    if (row > 0) {
                        traffic = trafficService.findById(id);
                        map.put("traffic", traffic);
                        map.put("ok", true);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case "delete": //删除
                row = trafficService.delete(id);
                if (row > 0) {
                    map.put("ok", true);
                }
                break;
        }
        return map;
    }
}
