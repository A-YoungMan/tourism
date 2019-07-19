package com.oaec.tourism.mapper;

import com.oaec.tourism.entity.Order;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface OrderMapper {
    int create(Order order);
    int createOrderAndContact(Map<String,Integer> map); //出游人信息
    Order findById(int id);
    List<Order> findByTripId(int id);//景点id查询所有订单
    List<Order> findByPlaceId(int id);//景区id查询所有订单
    List<Order> findByUserId(int userId);
    List<Order> findByCreateTime(@Param(value = "date") String date);//按日期查询
    List<Order> findAll();
}
