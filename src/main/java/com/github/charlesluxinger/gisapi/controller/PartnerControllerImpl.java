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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Charles Luxinger
 * @version 1.0.0 07/12/20
 */
@RestController
@AllArgsConstructor
public class PartnerControllerImpl implements PartnerController {

    private final PartnerService service;

    @Override
    @GetMapping(value = "/partner/{id}", produces = APPLICATION_JSON_VALUE)
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
                                        .status(HttpStatus.NOT_FOUND.ordinal())
                                        .title(HttpStatus.NOT_FOUND.getReasonPhrase())
                                        .detail(String.format("Partner #%s Not Found", id))
                                        .path(String.format("api/v1/partner/%s", id))
                                        .build()));
    }

    @Override
    @ResponseStatus(CREATED)
    @PostMapping(value = "/partner",consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Flux<PartnerResponse> saveAll(@RequestBody final Set<PartnerPayload> partners){
        return Flux
                .fromIterable(partners)
                .map(PartnerPayload::toDomain)
                .collectList()
                .flatMapMany(service::saveAll)
                .map(PartnerResponse::of);
    }

}
