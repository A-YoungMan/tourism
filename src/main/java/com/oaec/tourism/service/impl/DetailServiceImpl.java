package com.oaec.tourism.service.impl;

import com.oaec.tourism.entity.Detail;
import com.oaec.tourism.mapper.DetailMapper;
import com.oaec.tourism.service.DetailService;
import com.oaec.tourism.service.TripService;
import com.oaec.tourism.util.ContextPath;
import com.oaec.tourism.util.TxtPathUrlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DetailServiceImpl implements DetailService {
    @Autowired
    private DetailMapper detailMapper;
    @Autowired
    private TripService tripService;

    @Override
    public Map<String, Object> addDetail(int tripId, String place, String hotel, String food) {
        Map<String, Object> map = new HashMap<>();
        map.put("ok", false);
        Detail detail = new Detail();
        if (place != null && !place.equals("")) {
            //生成文本txt 景区
            String filePalceName = ContextPath.TXT_ABC_PATH + System.currentTimeMillis() + ".txt";
            TxtPathUrlUtil.outTxt(filePalceName, place);
            detail.setPlace(filePalceName);
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //酒店
            String FileHotelName = ContextPath.TXT_ABC_PATH + System.currentTimeMillis() + ".txt";
            TxtPathUrlUtil.outTxt(FileHotelName, hotel);
            detail.setHotel(FileHotelName);
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //美食
            String FileFoodName = ContextPath.TXT_ABC_PATH + System.currentTimeMillis() + ".txt";
            TxtPathUrlUtil.outTxt(FileFoodName, food);
            detail.setFood(FileFoodName);

            detail.setTrip(tripService.findByIdAndNum(tripId, null, null));
            int i = detailMapper.create(detail);
            if (i > 0) {
                map.put("success", "增加说明成功!");
                map.put("ok", true);
            } else {
                map.put("error", "增加说明失败!");
            }
        } else {
            map.put("error", "景点说明不能为空!");
        }
        return map;
    }

    @Override
    public int update(Detail detail) {
        Detail detailTwo = findByIdOrTrip(detail.getId(), null, false);
        try {
            if (detail.getPlace() != null) {
                //生成文本txt 景区
                TxtPathUrlUtil.outTxt(detailTwo.getPlace(), detail.getPlace());
                detail.setPlace(detailTwo.getPlace());
            }
            if (detail.getHotel() != null) {
                //酒店
                TxtPathUrlUtil.outTxt(detailTwo.getHotel(), detail.getHotel());
                detail.setHotel(detailTwo.getHotel());
            }
            if (detail.getFood() != null) {
                //美食
                TxtPathUrlUtil.outTxt(detailTwo.getFood(), detail.getFood());
                detail.setFood(detailTwo.getFood());
            }
        } catch (Exception e) {
        }
        return detailMapper.update(detail);
    }

    @Override
    public int deleteByIdOrTripId(Integer id, Integer tripId) {
        return detailMapper.deleteByIdOrTripId(id, tripId);
    }

    @Override
    public Detail findByIdOrTrip(Integer id, Integer tripId, Boolean analysis) {
        Detail detail = detailMapper.findByIdOrTripId(id, tripId);
        if (analysis) {//true解析
            String palce = null;
            String hotel = null;
            String food = null;
            try {
                if (detail.getPlace() != null && !detail.getPlace().trim().equals("")) {
                    palce = TxtPathUrlUtil.inpOrOutIo(detail.getPlace(), "景区详情未添加！点击添加");
                } else { //新增
                    palce = ContextPath.TXT_ABC_PATH + System.currentTimeMillis() + ".txt";
                    detail.setPlace(palce);
                    int row = update(detail);//新增或修改
                    if (row > 0) {
                        System.out.println("修改新增成功!");
                        palce = TxtPathUrlUtil.getPathTxt(detail.getPlace());
                    } else {
                        System.out.println("失败");
                    }
                }
                detail.setPlace(palce);

                if (detail.getHotel() != null && !detail.getHotel().trim().equals("")) {
                    hotel = TxtPathUrlUtil.inpOrOutIo(detail.getHotel(), "酒店详情未添加！点击添加");
                } else {//新增
                    hotel = ContextPath.TXT_ABC_PATH + System.currentTimeMillis() + ".txt";
                    detail.setHotel(hotel);
                    int row = update(detail);//新增或修改
                    if (row > 0) {
                        System.out.println("修改新增成功!");
                        hotel = TxtPathUrlUtil.getPathTxt(detail.getHotel());
                    } else {
                        System.out.println("失败");
                    }
                }
                detail.setHotel(hotel);
                if (detail.getFood() != null && !detail.getFood().trim().equals("")) {
                    food = TxtPathUrlUtil.inpOrOutIo(detail.getFood(), "美食详情未添加！点击添加");
                } else { //新增
                    food = ContextPath.TXT_ABC_PATH + System.currentTimeMillis() + ".txt";
                    detail.setFood(food);
                    int row = update(detail);//新增或修改
                    if (row > 0) {
                        System.out.println("修改新增成功!");
                        food = TxtPathUrlUtil.getPathTxt(detail.getFood());
                    } else {
                        System.out.println("失败");
                    }
                }
                detail.setFood(food);
            } catch (Exception e) {
            }
        }//不解析原样输出文本路径
        return detail;
    }

    @Override
    public List<Detail> findByAllOrPlaceId(Integer id) {
        return detailMapper.findByAllOrPlaceId(id);
    }
}
