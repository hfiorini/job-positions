package com.fetcher.positions.interceptor;

import com.fetcher.positions.entity.exception.ExternalApiClientErrorException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.WebUtils;

import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {
    /** Provides handling for exceptions throughout this service. */
    @ExceptionHandler({ ExternalApiClientErrorException.class })
    public final ResponseEntity handleException(Exception ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();

        if (ex instanceof ExternalApiClientErrorException) {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            ExternalApiClientErrorException e = (ExternalApiClientErrorException) ex;

            return handleExternalApiClientErrorException(e, headers, status, request);
        } else {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            return handleExceptionInternal(ex, null, headers, status, request);
        }
    }

    /** Customize the response for ExternalApiClientErrorException. */
    protected ResponseEntity handleExternalApiClientErrorException(ExternalApiClientErrorException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = Collections.singletonList("External API Url might be wrong. Please check and retry");
        return handleExceptionInternal(ex, errors, headers, status, request);
    }

    /** A single place to customize the response body of all Exception types. */
    protected ResponseEntity handleExceptionInternal(Exception ex, List<String> errors, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }

        return new ResponseEntity<>(errors, headers, status);
    }
}
