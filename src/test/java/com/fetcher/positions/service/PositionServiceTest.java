package com.fetcher.positions.service;

import com.fetcher.positions.entity.Position;
import com.fetcher.positions.entity.PositionDTO;
import com.fetcher.positions.entity.PositionType;
import com.fetcher.positions.entity.PositionView;
import com.fetcher.positions.repository.PositionRepository;
import com.fetcher.positions.service.impl.PositionServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

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
    private RestTemplate restTemplate;

    private PositionService positionService;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void GivenAnApiWhenIWantTOStore100PositionsThenRestTemplateIsCalledTwice(){
        PositionDTO[] dtos = generate50RandomItems();

        when(restTemplate.getForEntity(anyString(), eq(PositionDTO[].class))).thenReturn(new ResponseEntity<>(dtos, HttpStatus.OK));
        positionService = new PositionServiceImpl(restTemplate, repository);

        positionService.importAll("Service URL", 100);
        verify(restTemplate, times(2)).getForEntity(anyString(), any());
    }

    @Test
    public void GivenAnApiWhenIWantTOStoreLessThan50PositionsThenRestTemplateIsCalledJustOnce(){

        PositionDTO[] dtos = generate50RandomItems();

        when(restTemplate.getForEntity(anyString(), eq(PositionDTO[].class))).thenReturn(new ResponseEntity<>(dtos, HttpStatus.OK));
        positionService = new PositionServiceImpl(restTemplate, repository);

        positionService.importAll("Service URL", 48);
        verify(restTemplate, times(1)).getForEntity(anyString(), any());
    }

    @Test
    public void GivenAnApiWhenApiProvidesLessRecordsThanNeededThenJustStoreWhatICan(){
        PositionDTO positionDTO = new PositionDTO();
        positionDTO.setId("6c537fb7-cf27-40c7-b04b-08773b9a1197");
        PositionDTO[] dtos = {positionDTO};

        when(restTemplate.getForEntity(anyString(), eq(PositionDTO[].class))).thenReturn(new ResponseEntity<>(dtos, HttpStatus.OK));
        positionService = new PositionServiceImpl(restTemplate, repository);

        positionService.importAll("Service URL", 10);
        verify(restTemplate, times(1)).getForEntity(anyString(), any());
    }

    @Test
    public void GivenAnApiWhenIWantTOStore100PositionsThenRepositoryIsCalledTwice(){
        PositionDTO[] dtos = generate50RandomItems();

        when(restTemplate.getForEntity(anyString(), eq(PositionDTO[].class))).thenReturn(new ResponseEntity<>(dtos, HttpStatus.OK));
        positionService = new PositionServiceImpl(restTemplate, repository);

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


        when(repository.findByTypeAndLocationAndName(any(), anyString(), anyString())).thenReturn(positionList);
        positionService = new PositionServiceImpl(restTemplate, repository);

        positionService.findPositionBy("Full Time", "The Location", "Some Description");
        verify(repository).findByTypeAndLocationAndName(eq(PositionType.FULL_TIME), eq("The Location"), eq("Some Description"));
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


        when(repository.findByTypeAndLocationAndName(any(), anyString(), anyString())).thenReturn(positionList);
        positionService = new PositionServiceImpl(restTemplate, repository);

        List<PositionView> result = positionService.findPositionBy("Full Time", "The Location", "Some Description");

        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0).getId(), "6c537fb7-cf27-40c7-b04b-08773b9a1197");
        Assert.assertEquals(result.get(0).getCompany(), "The company");
        Assert.assertEquals(result.get(0).getLocation(), "San Francisco");
        Assert.assertEquals(result.get(0).getDescription(), "Developer");
        Assert.assertEquals(result.get(0).getType(), "Full Time");
    }

    private PositionDTO[] generate50RandomItems(){
        List<PositionDTO> dtos = new ArrayList<>();
        for (int i = 0; i < 49; i++) {
            PositionDTO positionDTO = new PositionDTO();
            positionDTO.setId(UUID.randomUUID().toString());
            dtos.add(positionDTO)  ;
        }
        return dtos.toArray(new PositionDTO[0]);
    }
}
