package com.oaec.tourism.service.impl;

import com.oaec.tourism.entity.Order;
import com.oaec.tourism.entity.Sequence;
import com.oaec.tourism.entity.User;
import com.oaec.tourism.mapper.OrderMapper;
import com.oaec.tourism.service.OrderService;
import com.oaec.tourism.service.PriceService;
import com.oaec.tourism.service.RegionService;
import com.oaec.tourism.service.TripService;
import org.omg.CORBA.ORB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private TripService tripService;
    @Autowired
    private PriceService priceService;
    @Autowired
    private RegionService regionService;

    @Override
    public int saveOrder(Sequence sequence, User user, Double total, int adult, int child,Integer [] contactId) {
        Map<String, Integer> sequenceKeying = sequenceKeying(sequence.getValue());
        Order order = new Order();
        order.setOrderNo(System.currentTimeMillis()+"");
        order.setTotalPrice(total);
        order.setAdultNum(adult);
        order.setChildNum(child);
        order.setCreateDate(new Date());
        order.setUser(user);
        
        Integer cityId = sequenceKeying.get("cityId");
        order.setGoPoint( regionService.findById(cityId).getName());
        Integer priceId = sequenceKeying.get("priceId");
        order.setStartDate(priceService.findById(priceId).getDate());
        Integer tripId = sequenceKeying.get("tripId");
        order.setTrip(tripService.findById(tripId));
        int row = orderMapper.create(order);
        if(row >0){
            Map<String,Integer> map = new HashMap<>();
            map.put("orderId",order.getId());
            for(int i =0;i<contactId.length;i++){
                if(i%2 == 0){
                    continue;
                }
                map.put("contactId",contactId[i]);
                row =orderMapper.createOrderAndContact(map);
                if(row>0){
                    System.out.println("订单关联游玩表插入成功!");
                }
            }
        }
        return row;
    }

    @Override
    public Order findById(int id) {
        return orderMapper.findById(id);
    }

    @Override
    public List<Order> findByTripId(int id) {
        return orderMapper.findByTripId(id);
    }

    @Override
    public List<Order> findByPlaceId(int id) {
        return orderMapper.findByPlaceId(id);
    }

    @Override
    public List<Order> findByUserId(int userId) {
        return orderMapper.findAll();
    }

    @Override
    public List<Order> findAll() {
        return orderMapper.findAll();
    }
    /**
     * 用于解析 sequenceKeying 里面的value
     * @param value
     * @return
     */
    public static Map<String,Integer> sequenceKeying(String value){
        Map<String,Integer> map = new HashMap<>();
        String[] split = value.split("&");
        for(int i =0;i<split.length;i++){
            String[] strings = split[i].split("=");
            map.put(strings[0],Integer.parseInt(strings[1]));
        }
        return map;
    }

    @Override
    public List<Order> findByCreateTime(String date) {
        return orderMapper.findByCreateTime(date);
    }
}
