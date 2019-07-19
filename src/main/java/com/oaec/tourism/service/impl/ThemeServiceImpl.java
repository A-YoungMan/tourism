package com.oaec.tourism.service.impl;

import com.oaec.tourism.entity.Theme;
import com.oaec.tourism.mapper.ThemeMapper;
import com.oaec.tourism.service.ThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ThemeServiceImpl implements ThemeService {
    @Autowired
    private ThemeMapper themeMapper;
    @Override
    public int createTheme(String name, String detail) {
        Theme theme = new Theme();
        theme.setName(name);
        theme.setType(0);
        theme.setDetail(detail);
        return themeMapper.create(theme);
    }

    @Override
    public int update(Theme theme) {
        return themeMapper.update(theme);
    }

    @Override
    public Theme findByName(String name) {
        return themeMapper.findByName(name);
    }
    @Override
    public Theme findById(int id, int type) {
        return themeMapper.findById(id,type);
    }

    @Override
    public List<Theme> findAll(int type) {
        return themeMapper.findAll(type);
    }

    @Override
    public List<Theme> findAllKey(String key, int type) {
        key = "'%"+key+"%'";
        return themeMapper.findAllKey(key,type);
    }

    @Override
    public int addRecovery(Integer id) {
        return themeMapper.addRecovery(id);
    }

    @Override
    public int returnList(Integer id) {
        return themeMapper.returnList(id);
    }

    @Override
    public int closeRecovery(Integer id) {
        return themeMapper.closeRecovery(id);
    }

    @Override
    public int createThemeOrTrip(Map<String, Integer> map) {
        return themeMapper.createThemeOrTrip(map);
    }

    @Override
    public int deleteThemeOrTrip(Map<String, Integer> map) {
        return themeMapper.deleteThemeOrTrip(map);
    }

    @Override
    public List<Theme> findByThemeIdOrTripId(Integer themeId, Integer tripId) {
        return themeMapper.findByThemeIdOrTripId(themeId,tripId);
    }
}
