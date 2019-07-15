package com.fetcher.positions.processor;

import com.fetcher.positions.entity.PositionDTO;
import com.fetcher.positions.entity.exception.ExternalApiClientErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ExternalApiProcessor extends AbstractExternalApiProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(ExternalApiProcessor.class);

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
        try {
            response =restTemplate.getForEntity(getUrl()+"?page="+page, PositionDTO[].class);
        } catch(HttpClientErrorException e){
            LOG.error(e.getMessage());
            throw new ExternalApiClientErrorException(HttpStatus.NOT_FOUND);
        }

        return (response.getBody() != null) ? Arrays.asList(response.getBody()) : new ArrayList<>();
    }

    @Override
    public Boolean isResponseOk() {
        return (response != null) && (response.getStatusCode() == HttpStatus.OK);
    }
}
