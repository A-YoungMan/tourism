package com.oaec.tourism.service.impl;

import com.oaec.tourism.entity.City;
import com.oaec.tourism.entity.Place;
import com.oaec.tourism.entity.Position;
import com.oaec.tourism.mapper.CityMapper;
import com.oaec.tourism.mapper.PlaceMapper;
import com.oaec.tourism.mapper.PositionMapper;
import com.oaec.tourism.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class PlaceServiceImpl implements PlaceService {
    @Autowired
    private PlaceMapper placeMapper;
    @Autowired
    private PositionMapper positionMapper;
    @Autowired
    private CityMapper cityMapper;
    @Override
    @CacheEvict(value = "place-key")
    public int addPlace(String name, String area, float p1, float p2,Integer cityId) {
        /*创建国家*/
        City city = new City();
        city.setId(cityId);
        //创建景点
        Place place = new Place();
        place.setName(name);
        place.setCity(city);
        place.setArea(area);
        place.setCreateTime(new Date());
        int i = placeMapper.create(place);
        if (i > 0) {
            Position position = new Position();
            position.setPlace(place);
            position.setP1(p1);
            position.setP2(p2);
            i = positionMapper.create(position);
        }
        return i;
    }

    @Override
    @CacheEvict(value = "place-key")
    public int update(Place place, Float p1, Float p2,Integer cityId) {
        int row = placeMapper.update(place.getId(),place.getName(),place.getArea(),cityId);
        if (row > 0) {
            Position position = new Position();
            position.setPlace(place);
            position.setP1(p1);
            position.setP2(p2);
            Position position1 = positionMapper.findByPlaceId(place.getId());
            if (position1 != null) {//修改
                row = positionMapper.update(position);
            } else {//新增
                row = positionMapper.create(position);
            }
        }
        return row;
    }

    @Override
    @CacheEvict(value = "place-key")
    public int delete(int id) {
        return placeMapper.delete(id);
    }

    @Override
    public Place findById(int id) {
        return placeMapper.findById(id);
    }

    @Override
    public List<Place> findAll(int type) {
        return placeMapper.findAll(type);
    }

    @Override
    public int updateAll(Integer id) {
        return placeMapper.updateAll(id);
    }

    @Override
    public int emptyAll(Integer id) {
        return placeMapper.emptyAll(id);
    }

    @Override
    public List<Place> findAllCityNameAndType(int type, String key) {
        key ="'%"+key+"%'";
        return placeMapper.findAllCityNameAndType(type,key);
    }

    @Override
    public List<Place> findAllName(int type, String key) {
        key ="'%"+key+"%'";
        return placeMapper.findAllName(type,key);
    }
}
