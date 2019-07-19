package com.oaec.tourism.service.impl;

import com.oaec.tourism.entity.Dates;
import com.oaec.tourism.entity.Trip;
import com.oaec.tourism.mapper.DatesMapper;
import com.oaec.tourism.mapper.TripMapper;
import com.oaec.tourism.service.DatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatesServiceImpl implements DatesService {
    @Autowired
    private TripMapper tripMapper;
    @Autowired
    private DatesMapper datesMapper;

    @Override
    public int update(Dates dates) {
        return datesMapper.update(dates);
    }

    @Override
    public Dates findById(int id) {
        return datesMapper.findById(id);
    }

    @Override
    //插叙前插入
    public List<Dates> findByTripId(Integer tripId) {
        Trip trip = tripMapper.findById(tripId); //查询出行天数
        List<Dates> list = datesMapper.findByTripId(tripId);
        int index = list.size(); //用于记录下标
        if (index >= trip.getTime()) {
            return list;
        } else {
            Dates dates = null;
            index = index +1;
            int row = 0;
            while (index <= trip.getTime()) {
                dates = new Dates();
                dates.setTripId(tripId);
                dates.setNum(index);
                dates.setPrompt("暂无提示");
                dates.setTitle("暂无标题");
                dates.setDetail("暂无详情");
                dates.setHotel("暂无信息");
                dates.setMeal("暂无信息");
                dates.setTraffic("暂无信息");
                row = datesMapper.create(dates);
                index++;
                System.out.println("插入----->>");
            }
            return datesMapper.findByTripId(tripId);
        }
    }
}
