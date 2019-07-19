package com.oaec.tourism.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oaec.tourism.entity.Comment;
import com.oaec.tourism.entity.Price;
import com.oaec.tourism.entity.Region;
import com.oaec.tourism.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 详情页操作
 */
@Controller
@RequestMapping(value = "/details")
public class DetailsController {
    @Autowired
    private PriceService priceService;
    @Autowired
    private RegionService regionService;
    @Autowired
    private CommentService commentService;

    /**
     * Ajax进入旅游详情页查询价格
     *
     * @param id
     * @param year
     * @param month
     * @return
     */
    @RequestMapping(value = "price", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> findDatePrice(@RequestParam(name = "id", required = true) Integer id,
                                             @RequestParam(name = "year", required = true) String year,
                                             @RequestParam(name = "month", required = false) String month) {
        List<Price> list = null;
        Map<String, Object> map = new HashMap<>();
        list = priceService.findByAllTripIdAndYearAndMonth(id, year, month);
        map.put("list", list);
        return map;
    }

    /**
     * Ajax选择发车城市
     *
     * @param act
     * @return
     */
    @RequestMapping(value = "city", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> findAllCity(String act) {
        Map<String, Object> map = new HashMap<>();
        List<Region> list = null;
        switch (act) {
            case "list":
                list = regionService.findProvinces();
                break;

        }
        map.put("list", list);
        return map;
    }

    /**
     * Ajax请求查询评论类型
     *
     * @param act    //
     * @param tripId
     * @return
     */
    @RequestMapping(value = "comment", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> ajaxCommentFindAll(String act,
                                                  @RequestParam(value = "tripId") Integer tripId,
                                                  @RequestParam(value = "type") Integer type,
                                                  @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum) {
        Map<String, Object> map = new HashMap<>();
        switch (act) {
            case "query"://查询
                if (type == 0) { //查询全部
                    PageHelper.startPage(0, 5);
                    List<Comment> list = commentService.findByTripOrType(tripId, null);
                    map.put("list", list);
                } else if (type == 1) {//好评
                    PageHelper.startPage(0, 5);
                    List<Comment> list = commentService.findByTripOrType(tripId, 0);
                    map.put("list", list);
                } else if (type == 2) { //一般
                    PageHelper.startPage(0, 5);
                    List<Comment> list = commentService.findByTripOrType(tripId, 1);
                    map.put("list",list);
                } else if (type == 3) { //差评
                    PageHelper.startPage(0, 5);
                    List<Comment> list = commentService.findByTripOrType(tripId, 2);
                    map.put("list",list);
                }
                break;
        }
        return map;
    }
}
