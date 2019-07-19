package com.oaec.tourism.service;

import com.oaec.tourism.entity.Dates;

import java.util.List;

public interface DatesService {
    int update(Dates dates);
    Dates findById(int id);
    List<Dates> findByTripId(Integer tripId);
}
