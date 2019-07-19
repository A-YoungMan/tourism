package com.oaec.tourism.mapper;

import com.oaec.tourism.entity.Trip;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TripMapper {
    int create(Trip trip);
    int addTripOnPlace(Map<String,Object> map);
    int update(Trip trip);
    int updateIsOk(@Param(value = "id") Integer id,@Param(value = "is") Integer is);//修改是否上架
    Trip findById(Integer id);
    Trip findByIdAndNum(@Param(value = "id")Integer id,@Param(value = "num")Integer num,@Param(value = "is")Integer is);//按id查找或按旅游编号查找
    List<Trip> findAllPlaceById(@Param(value = "placeId")int placeId,@Param(value = "is")Integer is,@Param(value = "typeId") Integer typeId);//按景点id查询
    List<Trip> findAllPlaceByName(@Param(value = "placeName")String placeName,@Param(value = "is")Integer is,@Param(value = "type") Integer type);//按景点名称查询
    List<Trip> findAllCityById(@Param(value = "cityId")int cityId,@Param(value = "is")int is);//按地区id查询
    List<Trip> findAll(@Param(value = "dType")int dType,@Param(value = "is")Integer is);//查询所有 0/现有 1：加入回收站的
    List<Trip> findAllType(@Param(value = "type")int type);//根据出游类型查询
    List<Trip> findAllThemeId(@Param(value = "themeId")int themeId);//根据主题类型查询
    /*回收站操作*/
    int addRecovery(@Param(value = "id") Integer id);//加入回收站
    int closeRecovery(@Param(value = "id")Integer id);//清空回收站
    int returnList(@Param(value = "id")Integer id);//将回收站的恢复至列表
}
