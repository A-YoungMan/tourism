package com.oaec.tourism.service;

import com.oaec.tourism.entity.Trip;

import java.util.List;
import java.util.Map;

public interface TripService {
    //Ajax添加
    Map<String, Object> addAjaxTrip(Trip trip, int placeId, String province, String cityName, String area);

    //表单添加
    Map<String, Object> addTrip(Trip trip, int placeId, Integer provinceId, Integer cityId, Integer areaId);

    int update(Trip trip, Integer province, Integer name, Integer area);

    int updateIsOk(Integer id, int is);//修改是否上架

    Trip findByIdAndNum(Integer id, Integer num, Integer is);//按id查找或按旅游编号查找

    Trip findById(int id);

    List<Trip> findAllPlaceById(int placeId, Integer is, Integer typeId);//按景点id查询

    List<Trip> findAllPlaceByName(String placeName, Integer is, Integer type);//按景点名称查询

    List<Trip> findAllCityById(int cityId, int is);//按地区id查询

    List<Trip> findAll(int dType, Integer is);//查询所有 0/现有 1：加入回收站的 //null查询所有

    List<Trip> findAllType(int type);//出游类型查询

    List<Trip> findAllThemeId(int themeId);//按主题id查询

    /*回收站操作*/
    int addRecovery(Integer id);//加入回收站

    int closeRecovery(Integer id);//清空回收站

    int returnList(Integer id);//将回收站的恢复至列表
}
