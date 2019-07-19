package com.oaec.tourism.mapper;

import com.oaec.tourism.entity.TripPicture;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripPictureMapper {
    int create(TripPicture picture);
    int update(@Param(value = "id") int id,@Param(value = "base64") String base64,@Param(value = "name")String name);
    int delete(int id);
    TripPicture findById(int id);
    /*根据景区ID和类型查询是否存在 0主图专用*/
    TripPicture findByTripIdANDType(@Param(value = "tripId")Integer tripId,@Param (value = "type")Integer type);
    List<TripPicture> findByAllTripId(int tripId);
}
