package com.fetcher.positions.service;

import com.fetcher.positions.entity.Position;
import com.fetcher.positions.entity.PositionDTO;
import com.fetcher.positions.entity.PositionType;
import com.fetcher.positions.entity.PositionView;
import com.fetcher.positions.processor.ExternalApiProcessor;
import com.fetcher.positions.repository.PositionRepository;
import com.fetcher.positions.service.impl.PositionServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class PositionServiceTest {

    @Mock
    private PositionRepository repository;
    @Mock
    private ExternalApiProcessor processor;

    private PositionService positionService;

    private Integer page = null;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void GivenAnApiWhenIWantTOStore100PositionsThenRestTemplateIsCalledTwice(){
        List<PositionDTO> dtos = generate50RandomItems();

        when(processor.getPositions(any())).thenReturn(dtos);
        when(processor.isResponseOk()).thenReturn(true);
        positionService = new PositionServiceImpl(processor, repository);

        positionService.importAll("Service URL", 100);
        verify(processor, times(2)).getPositions(any());
    }

    @Test
    public void GivenAnApiWhenIWantTOStoreLessThan50PositionsThenRestTemplateIsCalledJustOnce(){

        List<PositionDTO> dtos = generate50RandomItems();

        when(processor.getPositions(any())).thenReturn(dtos);
        when(processor.isResponseOk()).thenReturn(true);
        positionService = new PositionServiceImpl(processor, repository);

        positionService.importAll("Service URL", 48);
        verify(processor, times(1)).getPositions(any());
    }

    @Test
    public void GivenAnApiWhenApiProvidesLessRecordsThanNeededThenJustStoreWhatICan(){
        PositionDTO positionDTO = new PositionDTO();
        positionDTO.setId("6c537fb7-cf27-40c7-b04b-08773b9a1197");
        List<PositionDTO> dtos = new ArrayList<>();
        dtos.add(positionDTO);

        when(processor.getPositions(any())).thenReturn(dtos);
        when(processor.isResponseOk()).thenReturn(true);
        positionService = new PositionServiceImpl(processor, repository);

        positionService.importAll("Service URL", 10);
        verify(processor, times(1)).getPositions(any());
    }

    @Test
    public void GivenAnApiWhenIWantTOStore100PositionsThenRepositoryIsCalledTwice(){
        List<PositionDTO> dtos = generate50RandomItems();

        when(processor.getPositions(any())).thenReturn(dtos);
        when(processor.isResponseOk()).thenReturn(true);
        positionService = new PositionServiceImpl(processor, repository);

        positionService.importAll("Service URL", 100);
        verify(repository, times(2)).saveAll(anyCollection());
    }

    @Test
    public void GivenASearchWhenIPassTypeDescriptionAndLocationThenRepositoryIsCalledOnceWithThoseSameParams(){
        Position position = new Position();
        position.setLocation("San Francisco");
        position.setCurrentCompany("The company");
        position.setType(PositionType.FULL_TIME);
        position.setName("Developer");
        position.setExternalId(UUID.fromString("6c537fb7-cf27-40c7-b04b-08773b9a1197"));
        List<Position> positionList = new ArrayList<>();
        positionList.add(position);


        when(repository.findByTypeAndLocationAndName(any(), anyString(), anyString(), isNull())).thenReturn(positionList);
        positionService = new PositionServiceImpl(processor, repository);

        positionService.findPositionBy("Full Time", "The Location", "Some Description", page);
        verify(repository).findByTypeAndLocationAndName(eq(PositionType.FULL_TIME), eq("The Location"), eq("Some Description"), eq(null));
    }

    @Test
    public void GivenASearchWhenIPassTypeDescriptionAndLocationThenResultsAreMappedCorrectly(){
        Position position = new Position();
        position.setLocation("San Francisco");
        position.setCurrentCompany("The company");
        position.setType(PositionType.FULL_TIME);
        position.setName("Developer");
        position.setExternalId(UUID.fromString("6c537fb7-cf27-40c7-b04b-08773b9a1197"));
        List<Position> positionList = new ArrayList<>();
        positionList.add(position);


        when(repository.findByTypeAndLocationAndName(any(), anyString(), anyString(), isNull())).thenReturn(positionList);
        positionService = new PositionServiceImpl(processor, repository);

        List<PositionView> result = positionService.findPositionBy("Full Time", "The Location", "Some Description", page);

        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0).getId(), "6c537fb7-cf27-40c7-b04b-08773b9a1197");
        Assert.assertEquals(result.get(0).getCompany(), "The company");
        Assert.assertEquals(result.get(0).getLocation(), "San Francisco");
        Assert.assertEquals(result.get(0).getDescription(), "Developer");
        Assert.assertEquals(result.get(0).getType(), "Full Time");
    }

    private List<PositionDTO> generate50RandomItems(){
        List<PositionDTO> dtos = new ArrayList<>();
        for (int i = 0; i < 49; i++) {
            PositionDTO positionDTO = new PositionDTO();
            positionDTO.setId(UUID.randomUUID().toString());
            dtos.add(positionDTO)  ;
        }
        return dtos;
    }
}
