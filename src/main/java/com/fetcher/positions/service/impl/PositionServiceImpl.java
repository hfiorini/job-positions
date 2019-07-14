package com.fetcher.positions.service.impl;

import com.fetcher.positions.entity.Position;
import com.fetcher.positions.entity.PositionDTO;
import com.fetcher.positions.entity.PositionType;
import com.fetcher.positions.entity.PositionView;
import com.fetcher.positions.repository.PositionRepository;
import com.fetcher.positions.service.PositionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PositionServiceImpl implements PositionService {

    private static final Logger LOG = LoggerFactory.getLogger(PositionServiceImpl.class);

    private RestTemplate restTemplate;
    private PositionRepository repository;

    public PositionServiceImpl(@Autowired RestTemplate restTemplate,@Autowired PositionRepository repository) {
        this.restTemplate = restTemplate;
        this.repository = repository;
    }

    @Override
    public void importAll(String url, Integer count) {
        Integer recordsByPage = 50;
        int pageNumber = 1;
        Integer savedPositions = 0;

        while (savedPositions < count){
            Integer positionsLeft = count - savedPositions;
            ResponseEntity<PositionDTO[]> response = restTemplate.getForEntity(url+"?pages="+pageNumber, PositionDTO[].class);
            if (response.getStatusCode() == HttpStatus.OK && positionsLeft >= recordsByPage){
                repository.saveAll(mapToPosition(Objects.requireNonNull(response.getBody())));
                savedPositions = savedPositions + recordsByPage;
            }
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null && positionsLeft < recordsByPage){
                if (positionsLeft < response.getBody().length){
                    repository.saveAll(mapToPosition(Objects.requireNonNull(response.getBody())).subList(0, positionsLeft - 1));
                }else{
                    repository.saveAll(mapToPosition(Objects.requireNonNull(response.getBody())).subList(0, response.getBody().length));
                }

                savedPositions = count;
            }
        }
    }

    @Override
    public List<PositionView> findPositionBy(String type, String location, String description) {
        List<Position> results;
        results = repository.findByTypeAndLocationAndName(PositionType.fromString(type),location, description);

        return  results.stream().map(it ->
                new PositionView(it.getExternalId().toString(), it.getCurrentCompany(), it.getLocation(), it.getName(), it.getType().getValue()))
                .collect(Collectors.toList());

    }

    private List<Position> mapToPosition(PositionDTO ... positionDTOs){
        List<Position> positionList = new ArrayList<>();
        for (PositionDTO dto: positionDTOs) {
            Position position = new Position();
            position.setExternalId(UUID.fromString(dto.getId()));
            position.setName(dto.getTitle());
            position.setCurrentCompany(dto.getCompany());
            position.setType(PositionType.fromString(dto.getType()));
            position.setLocation(dto.getLocation());
            positionList.add(position);
        }
        return positionList;
    }
}
