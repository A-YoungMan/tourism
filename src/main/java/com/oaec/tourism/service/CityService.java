package com.oaec.tourism.service;

import com.oaec.tourism.entity.City;

import java.util.List;

public interface CityService {
    int create(String name,int type,City city);
    City findById(int id);
    int delete(int id);
    //按类型查找
    List<City> findByType(int type);
    List<City> findAll();
}
