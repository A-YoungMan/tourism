package com.oaec.tourism.mapper;

import com.oaec.tourism.entity.Position;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionMapper {
    int create(Position position);
    int update(Position position);
    int deletePlaceId(int id);
    Position findByPlaceId(int id);
    Position findById(int id);
}
