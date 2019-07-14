package com.fetcher.positions.service;

import com.fetcher.positions.entity.PositionView;

import java.util.List;

public interface PositionService {

    void importAll(String url, Integer count);

    List<PositionView> findPositionBy(String type, String location, String description);
}
