package com.oaec.tourism.mapper;

import com.oaec.tourism.entity.Contact;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactMapper {
    int create(Contact contact);
    int update(Contact contact);
    int delete(int id);
    Contact findById(int id);
    Contact findByName(@Param(value = "userId") Integer userId,@Param(value = "name")  String name);
    List<Contact> findByUserId(int userId);
}
