package com.oaec.tourism.service;

import com.oaec.tourism.entity.Detail;

import java.util.List;
import java.util.Map;

public interface DetailService {
    Map<String,Object> addDetail(int tripId, String place, String hotel, String food);
    int update(Detail detail);
    int deleteByIdOrTripId( Integer id,Integer tripId);//按id删除或按Tripid删除
    Detail findByIdOrTrip(Integer id,Integer tripId,Boolean analysis);//id查询//是否解析txt文本内容
    List<Detail> findByAllOrPlaceId(Integer id);//查询所有或按 景点Id查询
}
