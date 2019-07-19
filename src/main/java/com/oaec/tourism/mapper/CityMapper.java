package com.oaec.tourism.mapper;

import com.oaec.tourism.entity.City;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityMapper {
    int create(City city);
    City findById(int id);
    int delete(int id);
    //按类型查找
    List<City> findByType(int type);
    List<City> findAll();
}
