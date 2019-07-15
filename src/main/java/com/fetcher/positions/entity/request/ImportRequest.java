package com.fetcher.positions.entity.request;

public class ImportRequest {
    private String url;
    private Integer count;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
