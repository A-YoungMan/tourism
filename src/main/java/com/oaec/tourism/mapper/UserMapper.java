package com.oaec.tourism.mapper;

import com.oaec.tourism.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    int create(User user);
    int update(User user);
    List<User> findAll();//查询全部
    User findById(int id);//id查找
    User findByCommentUserId(int id);//评论查询id
    User findByUserPhone(String phone);//账号查询
}
