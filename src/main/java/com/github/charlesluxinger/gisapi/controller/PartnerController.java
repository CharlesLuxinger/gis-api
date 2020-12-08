package com.github.charlesluxinger.gisapi.controller;

import com.github.charlesluxinger.gisapi.controller.domain.model.ApiExceptionResponse;
import com.github.charlesluxinger.gisapi.controller.domain.model.PartnerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Charles Luxinger
 * @version 07/12/20
 */
@Tag(name = "Partner")
public interface PartnerController {

    @Operation(summary = "Get a Partner", responses = {
            @ApiResponse(responseCode = "200", description = "Return a Partner",  content = @Content(
                    schema =  @Schema(implementation = PartnerResponse.class), mediaType = APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", description = "Not Found Exception",  content = @Content(
                    schema =  @Schema(implementation = ApiExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE))
    })
    @Parameter(name = "id", in = PATH, required = true, description = "Partner Id", example = "1")
    Mono<ResponseEntity> findById(final String id);
}
