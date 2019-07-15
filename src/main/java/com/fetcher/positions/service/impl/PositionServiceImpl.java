package com.fetcher.positions.service.impl;

import com.fetcher.positions.entity.Position;
import com.fetcher.positions.entity.PositionDTO;
import com.fetcher.positions.entity.PositionType;
import com.fetcher.positions.entity.PositionView;
import com.fetcher.positions.processor.ExternalApiProcessor;
import com.fetcher.positions.repository.PositionRepository;
import com.fetcher.positions.service.PositionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PositionServiceImpl implements PositionService {

    private static final Logger LOG = LoggerFactory.getLogger(PositionServiceImpl.class);
    public static final int RECORDS_PER_PAGE = 50;
    public static final int STARTING_PAGE_NUMBER = 0;

    private ExternalApiProcessor processor;
    private PositionRepository repository;

    public PositionServiceImpl(@Autowired ExternalApiProcessor processor, @Autowired PositionRepository repository) {
        this.processor = processor;
        this.repository = repository;
    }

    @Override
    public void importAll(String url, Integer count) {
        processor.init(url);
        Integer recordsPerPage = RECORDS_PER_PAGE;
        int pageNumber = STARTING_PAGE_NUMBER;
        Integer savedPositions = 0;

        while (savedPositions < count){
            pageNumber++;
            Integer positionsLeft = count - savedPositions;
            List<PositionDTO> response = processor.getPositions(pageNumber);
            if (processor.isResponseOk() && positionsLeft >= recordsPerPage){
                repository.saveAll(mapToPosition(response));
                savedPositions = savedPositions + recordsPerPage;
            }
            if (processor.isResponseOk() && positionsLeft < recordsPerPage){
                if (positionsLeft < response.size()){
                    repository.saveAll(mapToPosition(response).subList(0, positionsLeft - 1));
                }else{
                    repository.saveAll(mapToPosition(response).subList(0, response.size()));
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

    private List<Position> mapToPosition(List<PositionDTO>  positionDTOs){
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
