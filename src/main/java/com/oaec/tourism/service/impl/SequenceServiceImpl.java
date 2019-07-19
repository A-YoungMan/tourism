package com.oaec.tourism.service.impl;

import com.oaec.tourism.entity.Sequence;
import com.oaec.tourism.mapper.SequenceMapper;
import com.oaec.tourism.service.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SequenceServiceImpl implements SequenceService {
    @Autowired
    private SequenceMapper sequenceMapper;
    @Override
    public int create(String value, String keying) {
        Sequence sequence = new Sequence();
        sequence.setValue(value);
        sequence.setKeying(keying);
        sequence.setType(0);
        sequence.setDescing("未结算!");
        return sequenceMapper.create(sequence);
    }

    @Override
    public int update(Sequence sequence) {
        return sequenceMapper.update(sequence);
    }

    @Override
    public int delete(int id) {
        return sequenceMapper.delete(id);
    }

    @Override
    public Sequence findById(int id) {
        return sequenceMapper.findById(id);
    }

    @Override
    public List<Sequence> findByKeying(String keying) {
        return sequenceMapper.findByKeying(keying);
    }

    @Override
    public List<Sequence> findAll(int type) {
        return sequenceMapper.findAll(type);
    }
}
