package com.oaec.tourism.service;

import com.oaec.tourism.entity.Place;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlaceService {
    int addPlace(String name,String area,float p1,float p2,Integer cityId);
    int update(Place place,Float p1,Float p2,Integer cityId);
    int delete(int id);
    //恢复回收站单个或所有
    int updateAll(Integer id);
    //删除回收站单个或所有
    int emptyAll(Integer id);
    /*按景点名称查询*/
    List<Place> findAllName(int type,String key);
    /*地区和名字查询*/
    List<Place> findAllCityNameAndType(int type,String key);
    Place findById(int id);
    List<Place> findAll(int type);
}
