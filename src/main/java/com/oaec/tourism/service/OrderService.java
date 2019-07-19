package com.oaec.tourism.service;

import com.oaec.tourism.entity.Order;
import com.oaec.tourism.entity.Sequence;
import com.oaec.tourism.entity.User;

import java.util.List;

public interface OrderService {
    int saveOrder(Sequence sequence, User user, Double total,int adult, int child,Integer [] contactId);//从序列中添加订单
    Order findById(int id);
    List<Order> findByTripId(int id);//景点id查询所有订单
    List<Order> findByPlaceId(int id);//景区id查询所有订单
    List<Order> findByUserId(int userId);
    List<Order> findByCreateTime(String date);
    List<Order> findAll();
}
