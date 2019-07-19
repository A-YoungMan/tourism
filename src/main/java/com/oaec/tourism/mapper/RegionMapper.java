package com.oaec.tourism.mapper;

import com.oaec.tourism.entity.Region;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RegionMapper {
    @Select("SELECT * FROM region_directory WHERE pid=1")
    List<Region> findProvinces();//查询所有
    @Select("SELECT id,name,pid FROM region_directory WHERE pid=#{pid}")
    List<Region> findByParentId(int pid);//按父id查询
    @Select("SELECT id,name,pid FROM region_directory WHERE id=#{id}")
    Region findById(int id);//按id查询
}
