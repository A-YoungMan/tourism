package com.oaec.tourism.service.impl;

import com.oaec.tourism.entity.City;
import com.oaec.tourism.mapper.CityMapper;
import com.oaec.tourism.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CityServiceImpl implements CityService {
    @Autowired
    private CityMapper cityMapper;

    @Override
    public int create(String name,int type,City city) {
        City city1 = new City();
        city1.setType(type);
        city1.setName(name);
        city1.setCity(city);
        return cityMapper.create(city1);
    }

    @Override
    public City findById(int id) {
        return cityMapper.findById(id);
    }

    @Override
    public int delete(int id) {
        return cityMapper.delete(id);
    }

    @Override
    public List<City> findByType(int type) {
        return cityMapper.findByType(type);
    }

    @Override
    public List<City> findAll() {
        return cityMapper.findAll();
    }
}
