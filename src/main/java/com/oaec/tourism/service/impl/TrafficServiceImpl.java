package com.oaec.tourism.service.impl;

import com.oaec.tourism.entity.Traffic;
import com.oaec.tourism.mapper.TrafficMapper;
import com.oaec.tourism.service.TrafficService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TrafficServiceImpl implements TrafficService {
    @Autowired
    private TrafficMapper trafficMapper;

    @Override
    public int createTraffic(String goPoint,String goTime,String returnPoint,String returnTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Traffic traffic = new Traffic();
        traffic.setGoPoint(goPoint);
        try {
            if(goTime !=null && !goTime.equals("")){
                traffic.setGoTime(sdf.parse(goTime));
            }
            if(returnTime !=null && !returnTime.equals("")){
                traffic.setReturnTime(sdf.parse(returnTime));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        traffic.setReturnPoint(returnPoint);
        return trafficMapper.create(traffic);
    }

    @Override
    public int createSchedule(Integer tripId, Integer trafficId) {
        Map<String,Object> map = new HashMap<>();
        map.put("tripId",tripId);
        map.put("trafficId",trafficId);
        return trafficMapper.createSchedule(map);
    }

    @Override
    public int update(Traffic traffic) {
        return trafficMapper.update(traffic);
    }

    @Override
    public int delete(int id) {
        return trafficMapper.delete(id);
    }

    @Override
    public Traffic findById(int id) {
        return trafficMapper.findById(id);
    }

    @Override
    public List<Traffic> findByTripId(Integer tripId) {
        return trafficMapper.findByTripId(tripId);
    }

    @Override
    public List<Traffic> findAll() {
        return trafficMapper.findAll();
    }
}
