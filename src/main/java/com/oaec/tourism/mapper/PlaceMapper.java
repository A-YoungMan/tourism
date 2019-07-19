package com.oaec.tourism.mapper;

import com.oaec.tourism.entity.Place;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceMapper {
    int create(Place place);
    int update(@Param("id") int id,
               @Param(value = "name")String name,
               @Param(value = "area")String area,
               @Param(value = "cityId")int cityId);
    /*加入回收站*/
    int delete(int id);
    Place findById(int id); //按id查询
    //恢复回收站单个或所有
    int updateAll(@Param(value = "id") Integer id);
    //删除回收站单个或所有
    int emptyAll(@Param(value = "id") Integer id);
    /*按状态查*/
    List<Place> findAll(@Param(value = "type") int type);
    /*地区和名字查询*/
    List<Place> findAllCityNameAndType(@Param(value = "type")int type,@Param(value = "key") String name);
    /*按景点名称查询*/
    List<Place> findAllName(@Param(value = "type")int type,@Param(value = "key")String key);
}
