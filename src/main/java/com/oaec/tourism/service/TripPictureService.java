package com.oaec.tourism.service;

import com.oaec.tourism.entity.TripPicture;

import java.util.List;

public interface TripPictureService {
    int createTripPicture(int tripId,String name,String base64,int type,Integer id);
    int update(int id,String base64,String name);
    int delete(int id);
    TripPicture findById(int id);
    List<TripPicture> findByAllTripId(int tripId);
}
