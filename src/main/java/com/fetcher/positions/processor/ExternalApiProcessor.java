package com.fetcher.positions.processor;

import com.fetcher.positions.entity.PositionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ExternalApiProcessor extends AbstractExternalApiProcessor {


    private RestTemplate restTemplate;

    public ExternalApiProcessor(@Autowired RestTemplate restTemplate) {
        super();
        this.restTemplate = restTemplate;
    }

    public  void init(String url){
        setUrl(url);
    }

    @Override
    public List<PositionDTO> getPositions(Integer page) {
        response =restTemplate.getForEntity(getUrl()+"?page="+page, PositionDTO[].class);
        return (response.getBody() != null) ? Arrays.asList(response.getBody()) : new ArrayList<>();
    }

    @Override
    public Boolean isResponseOk() {
        return (response != null) && (response.getStatusCode() == HttpStatus.OK);
    }
}
