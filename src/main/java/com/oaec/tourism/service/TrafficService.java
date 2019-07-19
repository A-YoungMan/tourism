package com.oaec.tourism.service;

import com.oaec.tourism.entity.Traffic;

import java.util.List;

public interface TrafficService {
    int createTraffic(String goPoint,String goTime,String returnPoint,String returnTime);
    int createSchedule(Integer tripId,Integer trafficId);
    int update(Traffic traffic);
    int delete(int id);
    Traffic findById(int id);
    List<Traffic> findByTripId(Integer tripId);
    List<Traffic> findAll();
}
