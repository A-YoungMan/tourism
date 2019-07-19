package com.oaec.tourism.service;

import com.oaec.tourism.entity.Theme;

import java.util.List;
import java.util.Map;

public interface ThemeService {
    int createTheme(String name,String detail);
    int update(Theme theme);
    Theme findByName(String name);
    Theme findById(int id,int type);
    List<Theme> findAll(int type);
    List<Theme> findAllKey(String key,int type);
    int addRecovery( Integer id);//加入回收站
    int returnList(Integer id);//单条恢复或全部恢复至列表,
    int closeRecovery(Integer id);//删除单条或清空回收站
    /*桥表操作*/
    int createThemeOrTrip(Map<String,Integer> map);//创建桥表
    int deleteThemeOrTrip(Map<String,Integer> map);//删除桥表
    //按主题id查询，或旅游id查询
    List<Theme> findByThemeIdOrTripId(Integer themeId,Integer tripId);
}
