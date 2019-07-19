package com.oaec.tourism.controller.admin.trip;

import com.oaec.tourism.entity.TripPicture;
import com.oaec.tourism.service.TripPictureService;
import com.oaec.tourism.util.ContextPath;
import com.oaec.tourism.util.ImgUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * 旅游项目景区图片Controller操作
 */
@Controller
@RequestMapping(value = "/admin/trip")
public class AdminTripPictureController {
    @Autowired
    private TripPictureService tripPictureService;

    /**
     * AJax请求查询当前景区Trip的所有图片
     * Post请求
     *
     * @param tripId //景区id
     * @return
     */
    @RequestMapping(value = "img/list", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> findImg(Integer tripId) {
        List<TripPicture> list = tripPictureService.findByAllTripId(tripId);
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        return map;
    }

    /**
     * 添加图片
     *
     * @param tripId
     * @param base64//base64字符串
     * @return
     * @type //上传图片类型0主图 1副图
     */
    @RequestMapping(value = "img/add", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addImg(@RequestParam(name = "tripId", required = true) Integer tripId,
                                      @RequestParam(name = "type", required = false) Integer type,
                                      @RequestParam(name = "base64", required = false) String base64,
                                      @RequestParam(name = "id",required = false) Integer id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ok", false);
        int row = tripPictureService.createTripPicture(tripId, "暂无名称", base64, type,id);
        if (row > 0) {
            List<TripPicture> tripPictureList = new ArrayList<>();
            List<TripPicture> list = tripPictureService.findByAllTripId(tripId);
            for (TripPicture tripPicture : list) {
                if (tripPicture.getType() == 0) {
                    if(type == 0 ){
                        map.put("tripPicture", tripPicture); //副图
                        break;
                    }
                }else{
                    tripPictureList.add(tripPicture);//子图
                }
            }
            map.put("tripPictureList",tripPictureList);
            map.put("ok", true);
        } else {
            map.put("error", "图片上传失败!");
        }
        return map;
    }

    /**
     * 根据图片id修改图片
     *
     * @param id
     * @param base64 //图片base64字符串
     * @param name
     * @return
     */
    @RequestMapping(value = "img/update", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> updateImg(@RequestParam(name = "id", required = true) Integer id,
                                         @RequestParam(name = "base64", required = false) String base64,
                                         @RequestParam(name = "name", required = false) String name) {
        Map<String,Object> map = new HashMap<>();
        map.put("ok",false);
        int row = tripPictureService.update(id, base64, name);
        if(row>0){
            map.put("ok",true);
            map.put("base64",base64);
            if(name !=null &&! name.equals("")){
                map.put("name",name);
            }
        }
        return map;
    }

    /**
     * 根据id删除一条数据
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "img/delete", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> deleteImg(int id) {
        Map<String,Object> map = new HashMap<>();
        map.put("ok",false);
        int row = tripPictureService.delete(id);
        if(row>0){
            map.put("ok",true);
        }
        return map;
    }
}
