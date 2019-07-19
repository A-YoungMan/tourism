package com.oaec.tourism.service;

import com.oaec.tourism.entity.Region;

import java.util.List;

public interface RegionService {
    List<Region> findProvinces();//查询所有
    List<Region> findByParentId(int pid);//按父id查询
    Region findById(int id);
}
