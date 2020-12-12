package com.github.charlesluxinger.gisapi.controller.model.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Schema(name = "Api Exception Response")
@Getter
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiExceptionResponse {

    @Schema(example = "999")
    @NotNull
    private final int status;

    @Schema(example = "Network crashes")
    @NotNull
    private final String title;

    @Schema(example = "Some network cable was broken.")
    private final String detail;

    @Schema(example = "/api/v1/resource/1")
    private final String path;

    @Schema(example = "2020-04-24T19:27:01.718Z")
    @NotNull
    private final OffsetDateTime timestamp = OffsetDateTime.now().truncatedTo(ChronoUnit.MILLIS);

    public static ResponseEntity<ApiExceptionResponse> buildNotFoundResponse(final String path, final String detail) {
        return ResponseEntity
                .status(NOT_FOUND)
                .contentType(APPLICATION_JSON)
                .body(ApiExceptionResponse
                        .builder()
                        .status(NOT_FOUND.value())
                        .title(NOT_FOUND.getReasonPhrase())
                        .detail(detail)
                        .path(path)
                        .build());
    }

    public static ResponseEntity<ApiExceptionResponse> buildBadRequestResponse(final String path, final String detail) {
        return ResponseEntity
                .badRequest()
                .contentType(APPLICATION_JSON)
                .body(ApiExceptionResponse
                        .builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .title(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .detail(detail)
                        .path(path)
                        .build());
    }

}
