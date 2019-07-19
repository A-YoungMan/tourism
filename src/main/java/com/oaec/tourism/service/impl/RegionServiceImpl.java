package com.oaec.tourism.service.impl;

import com.oaec.tourism.entity.Region;
import com.oaec.tourism.mapper.RegionMapper;
import com.oaec.tourism.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RegionServiceImpl implements RegionService {
    @Autowired
    private RegionMapper regionMapper;
    @Override
    public List<Region> findProvinces() {
        return regionMapper.findProvinces();
    }

    @Override
    public List<Region> findByParentId(int pid) {
        return regionMapper.findByParentId(pid);
    }

    @Override
    public Region findById(int id) {
        return regionMapper.findById(id);
    }
}
