package com.github.charlesluxinger.gisapi.controller;

import com.github.charlesluxinger.gisapi.controller.model.ApiExceptionResponse;
import com.github.charlesluxinger.gisapi.controller.model.PartnerPayload;
import com.github.charlesluxinger.gisapi.controller.model.PartnerResponse;
import com.github.charlesluxinger.gisapi.domain.service.PartnerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Charles Luxinger
 * @version 1.0.0 07/12/20
 */
@RestController
@RequestMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class PartnerControllerImpl implements PartnerController {

    private final PartnerService service;

    public static final String LONGITUDE = "long";
    public static final String LATITUDE = "lat";

    @Override
    @GetMapping(value = "/partner/{id}")
    public Mono<ResponseEntity> findById(@PathVariable final String id){
        return service
                .findById(id)
                .map(p -> ResponseEntity.ok(PartnerResponse.of(p)))
                .cast(ResponseEntity.class)
                .defaultIfEmpty(ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .contentType(APPLICATION_JSON)
                                .body(ApiExceptionResponse
                                        .builder()
                                        .status(HttpStatus.NOT_FOUND.value())
                                        .title(HttpStatus.NOT_FOUND.getReasonPhrase())
                                        .detail(String.format("Partner '#%s' Not Found", id))
                                        .path(String.format("api/v1/partner/%s", id))
                                        .build()));
    }

    @Override
    @GetMapping(value = "/partner")
    public Mono<ResponseEntity> findNearbyAndCoverageArea(@RequestParam(LONGITUDE) final double longitude,
                                                          @RequestParam(LATITUDE) final double latitude){
        return service
                .findNearbyAndCoverageArea(longitude, latitude)
                .map(p -> ResponseEntity.ok(PartnerResponse.of(p)))
                .cast(ResponseEntity.class)
                .defaultIfEmpty(ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .contentType(APPLICATION_JSON)
                        .body(ApiExceptionResponse
                                .builder()
                                .status(HttpStatus.NOT_FOUND.value())
                                .title(HttpStatus.NOT_FOUND.getReasonPhrase())
                                .detail(String.format("Not Found nearby Partner at long:%f lat:%f", longitude, latitude))
                                .path(String.format("api/v1/partner?long=%f&lat=%f", longitude, latitude))
                                .build()));
    }

    @Override
    @ResponseStatus(CREATED)
    @PostMapping(value = "/partner")
    public Mono<ResponseEntity> save(@RequestBody final PartnerPayload partners){
        return Mono
                .just(partners)
                .map(PartnerPayload::toDomain)
                .flatMap(service::insertIfNotExists)
                .map(p -> ResponseEntity.created(URI.create("api/v1/partner")).body(PartnerResponse.of(p)))
                .cast(ResponseEntity.class)
                .onErrorResume(err -> {
                    if (err.getClass() == IllegalStateException.class) {
                        return Mono.just(ResponseEntity
                                .badRequest()
                                .contentType(APPLICATION_JSON)
                                .body(ApiExceptionResponse
                                        .builder()
                                        .status(HttpStatus.BAD_REQUEST.value())
                                        .title(HttpStatus.BAD_REQUEST.getReasonPhrase())
                                        .detail(err.getLocalizedMessage())
                                        .path("api/v1/partner")
                                        .build()));
                    }

                    return Mono.error(err);
                });
    }

}
