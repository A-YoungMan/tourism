package com.oaec.tourism.mapper;

import com.oaec.tourism.entity.Theme;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ThemeMapper {
    int create(Theme theme);
    int update(Theme theme);
    Theme findByName(@Param(value = "name")String name);//根据名称查询
    Theme findById(@Param(value = "id") Integer id,@Param(value = "type") Integer type);//按id查询
    List<Theme> findAll(@Param(value = "type") Integer type);//查询所有0 列表中的所有，1回收站的所有
    /*关键字查询*/
    List<Theme> findAllKey(@Param(value = "key")String key,@Param(value = "type") Integer type);//查询所有0 列表中的所有，1回收站的所有
    /*回收站操作*/
    int addRecovery(@Param(value = "id") Integer id);//加入回收站
    int returnList(@Param(value = "id") Integer id);//单条恢复或全部恢复至列表,
    int closeRecovery(@Param(value = "id") Integer id);//删除单条或清空回收站
    /*创建桥表Theme与Trip 桥表操作*/
    int createThemeOrTrip(Map<String,Integer> map);
    int deleteThemeOrTrip(Map<String,Integer> map);//删除桥表
    //按主题id查询，或旅游id查询
    List<Theme> findByThemeIdOrTripId(@Param(value = "themeId")Integer themeId,@Param(value = "tripId")Integer tripId);
    List<Theme> findByTripId(@Param(value = "id")Integer id);
}
