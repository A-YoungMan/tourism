package com.oaec.tourism.mapper;

import com.oaec.tourism.entity.Traffic;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TrafficMapper {
    int create(Traffic traffic);
    int createSchedule(Map<String,Object> map);//创建桥表
    int update(Traffic traffic);
    int delete(int id);
    Traffic findById(int id);
    List<Traffic> findByTripId(Integer tripId);
    List<Traffic> findAll();
}
