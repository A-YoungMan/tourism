package com.oaec.tourism.service.impl;

import com.github.pagehelper.PageHelper;
import com.oaec.tourism.entity.Comment;
import com.oaec.tourism.entity.Order;
import com.oaec.tourism.entity.User;
import com.oaec.tourism.mapper.CommentMapper;
import com.oaec.tourism.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Override
    public int createComment(Comment comment) {
        return commentMapper.create(comment);//插入成功后返回自身id
    }

    @Override
    public int delete(int id) {
        return commentMapper.delete(id);
    }

    @Override
    public Comment findById(int id) {
        return commentMapper.findById(id);
    }

    @Override
    public List<Comment> findByUserIdOrTripId(Integer userId, Integer tripId) {
        return commentMapper.findByUserIdOrTripId(userId, tripId);
    }

    @Override
    public List<Comment> findByTripOrType(Integer tripId, Integer type) {

        return commentMapper.findByTripOrType(tripId, type);
    }

    @Override
    public List<Comment> findByPlaceIdAndTypeAndTripType(Integer placeId, Integer type, Integer tripType) {
        return commentMapper.findByPlaceIdAndTypeAndTripType(placeId, type, tripType);
    }

    @Override
    public List<Comment> findAll() {
        return commentMapper.findAll();
    }
}
