package com.oaec.tourism.mapper;

import com.oaec.tourism.entity.Sequence;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SequenceMapper {
    int create(Sequence sequence);
    int update(Sequence sequence);
    int delete(int id);
    Sequence findById(int id);
    List<Sequence> findByKeying(String keying);
    List<Sequence> findAll(int type);
}
