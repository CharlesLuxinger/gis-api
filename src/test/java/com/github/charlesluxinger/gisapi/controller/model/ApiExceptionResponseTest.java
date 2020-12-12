package com.github.charlesluxinger.gisapi.controller.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Class comments go here...
 *
 * @author Charles Luxinger
 * @version 11/12/20
 */
class ApiExceptionResponseTest {

    private static final String PATH = "path/";
    private static final String DETAIL_MESSAGE = "detail message";

    @Test
    void should_return_a_not_found_ApiNotFoundResponse() {
        var response = ApiExceptionResponse.buildNotFoundResponse(PATH, DETAIL_MESSAGE).getBody();

        assertEquals(response.getDetail(), DETAIL_MESSAGE);
        assertEquals(response.getPath(), PATH);
        assertEquals(response.getStatus(), NOT_FOUND.value());
        assertEquals(response.getTitle(), NOT_FOUND.getReasonPhrase());
    }

    @Test
    void should_return_a_bad_request_ApiNotFoundResponse() {
        var response = ApiExceptionResponse.buildBadRequestResponse(PATH, DETAIL_MESSAGE).getBody();

        assertEquals(response.getDetail(), DETAIL_MESSAGE);
        assertEquals(response.getPath(), PATH);
        assertEquals(response.getStatus(), BAD_REQUEST.value());
        assertEquals(response.getTitle(), BAD_REQUEST.getReasonPhrase());
    }
}