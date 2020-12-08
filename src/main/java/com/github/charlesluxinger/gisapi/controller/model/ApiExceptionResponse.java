package com.github.charlesluxinger.gisapi.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

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

}
