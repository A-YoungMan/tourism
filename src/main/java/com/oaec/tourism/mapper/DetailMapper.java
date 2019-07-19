package com.oaec.tourism.mapper;

import com.oaec.tourism.entity.Detail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailMapper {
    int create(Detail detail);
    int update(Detail detail);
    int deleteByIdOrTripId(@Param(value = "id") Integer id,@Param(value = "tripId") Integer tripId);//按id删除或按Tripid删除
    Detail findByIdOrTripId(@Param(value = "id") Integer id,@Param(value = "tripId") Integer tripId);//id查询
    List<Detail> findByAllOrPlaceId(@Param(value = "id")Integer id);//查询所有或按 景点Id查询
}
