package com.github.charlesluxinger.gisapi.controller;

import com.github.charlesluxinger.gisapi.controller.model.ApiExceptionResponse;
import com.github.charlesluxinger.gisapi.controller.model.PartnerPayload;
import com.github.charlesluxinger.gisapi.controller.model.PartnerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;
import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Charles Luxinger
 * @version 07/12/20
 */
@Tag(name = "Partner")
public interface PartnerController {

    @Operation(summary = "Get a Partner by Id", responses = {
            @ApiResponse(responseCode = "200", description = "Return a Partner",  content = @Content(
                    schema =  @Schema(implementation = PartnerResponse.class), mediaType = APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", description = "Not Found Exception",  content = @Content(
                    schema =  @Schema(implementation = ApiExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE))
    })
    @Parameter(name = "id", in = PATH, required = true, description = "Partner Id", example = "1")
    Mono<ResponseEntity> findById(final String id);

    @Operation(summary = "Get a nearby Partner", responses = {
            @ApiResponse(responseCode = "200", description = "Return a nearby Partner",  content = @Content(
                    schema =  @Schema(implementation = PartnerResponse.class), mediaType = APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", description = "Not Found Exception",  content = @Content(
                    schema =  @Schema(implementation = ApiExceptionResponse.class), mediaType = APPLICATION_JSON_VALUE))
    })
    @Parameter(name = "longitude", in = QUERY, required = true, description = "User longitude position", example = "1")
    @Parameter(name = "latitude", in = QUERY, required = true, description = "User latitude position", example = "1")
    Mono<ResponseEntity> findNearbyByLongitudeAndLatitude(final double longitude,
                                                          final double latitude);

    @Operation(summary = "Save a List of Partners", responses = {
            @ApiResponse(responseCode = "201", description = "Created a List of Partners",  content = @Content(
                    schema =  @Schema(implementation = PartnerResponse.class), mediaType = APPLICATION_JSON_VALUE))
    })
    Flux<PartnerResponse> saveAll(@RequestBody Set<PartnerPayload> partners);
}
