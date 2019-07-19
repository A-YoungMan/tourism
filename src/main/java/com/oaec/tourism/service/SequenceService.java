package com.oaec.tourism.service;

import com.oaec.tourism.entity.Sequence;

import java.util.List;

public interface SequenceService {
    int create(String value,String keying);
    int update(Sequence sequence);
    int delete(int id);
    Sequence findById(int id);
    List<Sequence> findByKeying(String keying);
    List<Sequence> findAll(int type);
}
