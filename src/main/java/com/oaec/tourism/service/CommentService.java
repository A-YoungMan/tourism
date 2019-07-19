package com.oaec.tourism.service;

import com.oaec.tourism.entity.Comment;
import com.oaec.tourism.entity.Order;
import com.oaec.tourism.entity.User;

import java.lang.reflect.Type;
import java.util.List;

public interface CommentService {
    int createComment(Comment comment);
    int delete(int id);
    Comment findById( int id);
    List<Comment> findByUserIdOrTripId(Integer userId,Integer tripId);
    List<Comment> findByTripOrType(Integer tripId,Integer type);
    List<Comment> findByPlaceIdAndTypeAndTripType(Integer placeId,Integer type,Integer tripType);
    List<Comment> findAll();
}
