package com.oaec.tourism.service;

import com.oaec.tourism.entity.Contact;

import java.util.List;

public interface ContactService {
    int create(Contact contact);
    int update(Contact contact);
    int delete(int id);
    Contact findById(int id);
    Contact findByName(Integer userId,String name);
    List<Contact> findByUserId(int userId);
}
