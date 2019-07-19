package com.oaec.tourism.service;

import com.oaec.tourism.entity.Price;

import java.util.List;
import java.util.Map;

public interface PriceService {
    /*输入进来的日期值*/
    Map<String,Object> createPrice(Integer tripId,String dateBetween,float price);
    int update(Integer id,Float price);
    int delete(int id);//删除
    int deleteDate(Integer year,Integer month,Integer tripId);
    Price findById(int id);
    //根据日期和景区名称查询价格
    Price findByDateAndTripId(String dateBetween,Integer tripId);
    List<Price> findByAllTripId(int id);//根据景区id查询
    List<Price> findByAllTripIdAndYearAndMonth(int tripId,String year,String month);
}
