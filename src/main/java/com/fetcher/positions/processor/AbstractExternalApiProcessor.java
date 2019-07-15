package com.fetcher.positions.processor;

import com.fetcher.positions.entity.PositionDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public abstract class AbstractExternalApiProcessor {

    private String url;
    ResponseEntity<PositionDTO[]> response;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public abstract List<PositionDTO> getPositions(Integer page);
    public abstract Boolean isResponseOk();
}
