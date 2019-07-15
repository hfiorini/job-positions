package com.fetcher.positions.entity.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.charset.Charset;

public class ExternalApiClientErrorException extends HttpClientErrorException {
    public ExternalApiClientErrorException(HttpStatus statusCode) {
        super(statusCode);
    }

    public ExternalApiClientErrorException(HttpStatus statusCode, String statusText) {
        super(statusCode, statusText);
    }

    public ExternalApiClientErrorException(HttpStatus statusCode, String statusText, byte[] body, Charset responseCharset) {
        super(statusCode, statusText, body, responseCharset);
    }

    public ExternalApiClientErrorException(HttpStatus statusCode, String statusText, HttpHeaders headers, byte[] body, Charset responseCharset) {
        super(statusCode, statusText, headers, body, responseCharset);
    }
}
