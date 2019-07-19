package com.oaec.tourism.mapper;

import com.oaec.tourism.entity.Price;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PriceMapper {
    int create(Price price);
    int update(@Param(value = "id") Integer id, @Param(value = "price")Float price);
    int delete(int id);//删除
    int deleteDate(@Param(value = "tripId")Integer tripId,@Param(value = "startDate")String startDate,@Param(value = "endDate")String endDate);//按景区的id和日期范围删除删除
    Price findById(int id);
    //根据日期和景区名称查询价格
    Price findByDateAndTripId(@Param(value = "date")Date date,@Param(value = "tripId") Integer tripId);
    List<Price> findByAllTripId(int id);//根据景区id查询
    List<Price> findByAllTripIdAndYearAndMonth(@Param(value = "tripId") int tripId,@Param(value = "yearAndMonth")String yearAndMonth);//根据景区id查询也年份月份查询
}
