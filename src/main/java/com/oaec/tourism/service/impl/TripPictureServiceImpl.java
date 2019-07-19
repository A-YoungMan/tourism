package com.oaec.tourism.service.impl;

import com.oaec.tourism.entity.TripPicture;
import com.oaec.tourism.mapper.TripPictureMapper;
import com.oaec.tourism.service.TripPictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class TripPictureServiceImpl implements TripPictureService {
    @Autowired
    private TripPictureMapper tripPictureMapper;
    @Override
    public int createTripPicture(int tripId, String name, String base64, int type,Integer id) {
        TripPicture picture =null;
        if(type ==0){ //查询是否存主图，主图存在则修改
            picture= tripPictureMapper.findByTripIdANDType(tripId, type);
        }
        if(picture ==null){
            TripPicture tripPicture = new TripPicture();
            tripPicture.setTripId(tripId);
            tripPicture.setName(name);
            tripPicture.setBase64(base64);
            tripPicture.setType(type);
            return tripPictureMapper.create(tripPicture);
        }else{//修改
            return update(id,null,base64);
        }
    }

    @Override
    public int update(int id, String base64,String name) {
        return tripPictureMapper.update(id,base64,name);
    }

    @Override
    public int delete(int id) {
        return tripPictureMapper.delete(id);
    }

    @Override
    public TripPicture findById(int id) {
        return tripPictureMapper.findById(id);
    }

    @Override
    public List<TripPicture> findByAllTripId(int tripId) {
        return tripPictureMapper.findByAllTripId(tripId);
    }
}
