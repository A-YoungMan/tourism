package com.oaec.tourism.service.impl;

import com.oaec.tourism.entity.Contact;
import com.oaec.tourism.mapper.ContactMapper;
import com.oaec.tourism.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ContactServiceImpl implements ContactService {
    @Autowired
    private ContactMapper contactMapper;
    @Override
    public int create(Contact contact) {
        contactMapper.create(contact);
        return contact.getId()!=null?contact.getId():0;
    }

    @Override
    public int update(Contact contact) {
        return contactMapper.update(contact);
    }

    @Override
    public int delete(int id) {
        return contactMapper.delete(id);
    }

    @Override
    public Contact findById(int id) {
        return contactMapper.findById(id);
    }

    @Override
    public Contact findByName(Integer userId,String name) {
        return contactMapper.findByName(userId,name);
    }

    @Override
    public List<Contact> findByUserId(int userId) {
        return contactMapper.findByUserId(userId);
    }
}
