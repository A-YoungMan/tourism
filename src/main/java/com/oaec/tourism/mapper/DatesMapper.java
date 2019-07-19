package com.oaec.tourism.mapper;

import com.oaec.tourism.entity.Dates;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatesMapper {
    int create(Dates dates);
    int update(Dates dates);
    Dates findById(Integer id);
    List<Dates> findByTripId(@Param(value = "tripId") Integer tripId);
}