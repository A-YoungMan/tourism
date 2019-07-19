package com.oaec.tourism.mapper;

import com.oaec.tourism.entity.Comment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentMapper {
    int create(Comment comment);
    int delete(int id);
    Comment findById( int id);
    List<Comment> findByUserIdOrTripId(@Param(value = "userId") Integer userId,@Param(value = "tripId")Integer tripId);
    List<Comment> findAll();
    List<Comment> findByPlaceIdAndTypeAndTripType(@Param(value = "placeId")Integer placeId,@Param(value = "type")Integer type,@Param(value = "tripType")Integer tripType);
    //按景点id 和评论类型查询
    List<Comment> findByTripOrType(@Param(value = "tripId")Integer tripId,@Param(value = "type") Integer type);
}
